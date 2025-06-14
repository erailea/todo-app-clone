<template>
  <div class="note-card" 
       :class="{ 
         'done': note.done,
         'due-today': isDueToday && !note.done,
         'overdue': isOverdue && !note.done
       }">
    <!-- Drag Handle -->
    <div class="drag-handle" title="Drag to move">
      <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="9" cy="12" r="1"/>
        <circle cx="9" cy="5" r="1"/>
        <circle cx="9" cy="19" r="1"/>
        <circle cx="15" cy="12" r="1"/>
        <circle cx="15" cy="5" r="1"/>
        <circle cx="15" cy="19" r="1"/>
      </svg>
    </div>
    
    <div class="note-content" @click="toggleDone" :class="{ 'loading': loading.toggle }">
      <div class="note-checkbox">
        <div v-if="loading.toggle" class="loading-spinner small"></div>
        <div v-else-if="note.done" class="checkmark">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
            <polyline points="20,6 9,17 4,12"/>
          </svg>
        </div>
      </div>
      
      <div class="note-text" :class="{ 'strikethrough': note.done }">
        {{ note.content }}
      </div>
      
      <div v-if="note.dueDate" class="note-due-date" 
           :class="{
             'due-today-badge': isDueToday && !note.done,
             'overdue-badge': isOverdue && !note.done
           }">
        {{ formatDate(note.dueDate) }}
      </div>
    </div>
    
    <div class="note-actions">
      <button @click="editNote" class="edit-btn" title="Edit Note" :disabled="anyLoading">
        <div v-if="loading.update" class="loading-spinner"></div>
        <svg v-else width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
          <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
        </svg>
      </button>
      
      <button @click="deleteNoteAction" class="delete-btn" title="Delete Note" :disabled="anyLoading">
        <div v-if="loading.delete" class="loading-spinner"></div>
        <svg v-else width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="3,6 5,6 21,6"/>
          <path d="M19,6V20a2,2 0 0,1 -2,2H7a2,2 0 0,1 -2,-2V6M8,6V4a2,2 0 0,1 2,-2h4a2,2 0 0,1 2,2V6"/>
        </svg>
      </button>
    </div>

    <!-- Edit Modal -->
    <div v-if="showEditModal" class="modal-overlay" @click="closeEditModal">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>Edit Note</h3>
          <button @click="closeEditModal" class="close-btn">×</button>
        </div>
        <form @submit.prevent="updateNoteContent" class="modal-form">
          <textarea
            v-model="editContent"
            placeholder="Enter note content..."
            required
            rows="3"
            autofocus
          ></textarea>
          <input
            v-model="editDueDate"
            type="date"
            class="date-input"
            placeholder="Due date (optional)"
          />
          <div class="modal-actions">
            <button type="button" @click="closeEditModal" class="cancel-btn" :disabled="loading.update">
              Cancel
            </button>
            <button type="submit" class="save-btn" :disabled="!editContent.trim() || loading.update">
              <div v-if="loading.update" class="loading-spinner small"></div>
              <span v-else>Save</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'NoteCard',
  props: {
    note: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      showEditModal: false,
      editContent: '',
      editDueDate: '',
      loading: {
        update: false,
        delete: false,
        toggle: false
      }
    }
  },
  computed: {
    isDueToday() {
      if (!this.note.dueDate) return false
      const today = new Date().toISOString().split('T')[0]
      return this.note.dueDate.split('T')[0] === today
    },
    isOverdue() {
      if (!this.note.dueDate) return false
      const today = new Date().toISOString().split('T')[0]
      return new Date(this.note.dueDate.split('T')[0]) < new Date(today)
    },
    anyLoading() {
      return this.loading.update || this.loading.delete || this.loading.toggle
    }
  },
  methods: {
    ...mapActions(['updateNote', 'deleteNote']),
    
    async toggleDone() {
      if (this.loading.toggle) return
      
      this.loading.toggle = true
      try {
        await this.updateNote({
          noteId: this.note.id,
          noteData: {
            done: !this.note.done
          }
        })
        this.$emit('updated')
      } catch (error) {
        console.error('Error updating note:', error)
        alert('Failed to update note. Please try again.')
      } finally {
        this.loading.toggle = false
      }
    },
    
    editNote() {
      if (this.anyLoading) return
      
      this.editContent = this.note.content
      this.editDueDate = this.note.dueDate ? this.note.dueDate.split('T')[0] : ''
      this.showEditModal = true
      this.$emit('modal-opened')
    },
    
    closeEditModal() {
      this.showEditModal = false
      this.editContent = ''
      this.editDueDate = ''
      this.loading.update = false
      this.$emit('modal-closed')
    },
    
    async updateNoteContent() {
      if (!this.editContent.trim() || this.loading.update) return
      
      this.loading.update = true
      try {
        await this.updateNote({
          noteId: this.note.id,
          noteData: {
            content: this.editContent.trim(),
            dueDate: this.editDueDate ? new Date(this.editDueDate).toISOString() : null
          }
        })
        this.closeEditModal()
        this.$emit('updated')
      } catch (error) {
        console.error('Error updating note:', error)
        alert('Failed to update note. Please try again.')
      } finally {
        this.loading.update = false
      }
    },
    
    async deleteNoteAction() {
      if (this.loading.delete) return
      
      if (!confirm('Are you sure you want to delete this note?')) {
        return
      }
      
      this.loading.delete = true
      try {
        await this.deleteNote({
          noteId: this.note.id,
          listId: this.note.listId
        })
        this.$emit('deleted')
      } catch (error) {
        console.error('Error deleting note:', error)
        alert('Failed to delete note. Please try again.')
      } finally {
        this.loading.delete = false
      }
    },
    
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric'
      })
    }
  }
}
</script>

<style scoped>
.note-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  transition: all 0.2s;
  gap: 0.5rem;
}

.note-card:hover {
  border-color: #cbd5e0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.note-card.done {
  background: #f7fafc;
  border-color: #a0aec0;
}

/* Due date styling */
.note-card.due-today {
  background: linear-gradient(135deg, #fefce8 0%, #fef9c3 100%);
  border-color: #fbbf24;
  border-left: 4px solid #fbbf24;
}

.note-card.overdue {
  background: linear-gradient(135deg, #fef2f2 0%, #fee2e2 100%);
  border-color: #f87171;
  border-left: 4px solid #f87171;
}

.note-card.due-today:hover {
  border-color: #f59e0b;
  box-shadow: 0 4px 6px -1px rgba(251, 191, 36, 0.15);
}

.note-card.overdue:hover {
  border-color: #ef4444;
  box-shadow: 0 4px 6px -1px rgba(248, 113, 113, 0.15);
}

.drag-handle {
  color: #cbd5e0;
  cursor: grab;
  padding: 0.25rem;
  border-radius: 4px;
  transition: all 0.2s;
  opacity: 0;
  flex-shrink: 0;
}

.note-card:hover .drag-handle {
  opacity: 1;
  color: #a0aec0;
}

.drag-handle:hover {
  background: #f7fafc;
  color: #718096;
}

.drag-handle:active {
  cursor: grabbing;
}

.note-content {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
  min-width: 0;
  cursor: pointer;
}

.note-content.loading {
  opacity: 0.6;
  pointer-events: none;
}

.note-checkbox {
  width: 20px;
  height: 20px;
  border: 2px solid #cbd5e0;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  flex-shrink: 0;
}

.note-card.done .note-checkbox {
  background: #48bb78;
  border-color: #48bb78;
  color: white;
}

.note-card:not(.done) .note-checkbox:hover {
  border-color: #a0aec0;
}

.checkmark {
  display: flex;
  align-items: center;
  justify-content: center;
}

.note-text {
  flex: 1;
  font-size: 0.875rem;
  color: #2d3748;
  line-height: 1.4;
  word-break: break-word;
  transition: all 0.2s;
}

.note-text.strikethrough {
  text-decoration: line-through;
  color: #a0aec0;
}

.note-due-date {
  font-size: 0.75rem;
  color: #718096;
  background: #edf2f7;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.note-due-date.due-today-badge {
  background: #fde047;
  color: #a16207;
  font-weight: 500;
}

.note-due-date.overdue-badge {
  background: #fca5a5;
  color: #b91c1c;
  font-weight: 500;
}

.note-actions {
  display: flex;
  gap: 0.25rem;
  opacity: 0;
  transition: opacity 0.2s;
}

.note-card:hover .note-actions {
  opacity: 1;
}

.edit-btn, .delete-btn {
  padding: 0.375rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-btn {
  background: #f0f9ff;
  color: #0369a1;
}

.edit-btn:hover {
  background: #e0f2fe;
  color: #0c4a6e;
}

.delete-btn {
  background: #fef2f2;
  color: #dc2626;
}

.delete-btn:hover {
  background: #fee2e2;
  color: #b91c1c;
}

.edit-btn:disabled,
.delete-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  width: 90%;
  max-width: 400px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.modal-header h3 {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #718096;
  cursor: pointer;
  padding: 0.25rem;
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.modal-form textarea {
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 0.875rem;
  font-family: inherit;
  resize: vertical;
  min-height: 80px;
}

.modal-form textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.modal-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: flex-end;
}

.cancel-btn, .save-btn {
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.cancel-btn {
  background: #f7fafc;
  border: 1px solid #e2e8f0;
  color: #718096;
}

.cancel-btn:hover {
  background: #edf2f7;
}

.save-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
}

.save-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.save-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.date-input {
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 0.875rem;
  font-family: inherit;
  resize: vertical;
  min-height: 40px;
}

.date-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid #cbd5e0;
  border-top: 2px solid #48bb78;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.loading-spinner.small {
  width: 12px;
  height: 12px;
  border-width: 1.5px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 