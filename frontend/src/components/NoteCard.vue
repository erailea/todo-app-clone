<template>
  <div class="note-card" :class="{ 'done': note.done }">
    <div class="note-content" @click="toggleDone">
      <div class="note-checkbox">
        <div v-if="note.done" class="checkmark">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
            <polyline points="20,6 9,17 4,12"/>
          </svg>
        </div>
      </div>
      
      <div class="note-text" :class="{ 'strikethrough': note.done }">
        {{ note.content }}
      </div>
      
      <div v-if="note.dueDate" class="note-due-date">
        {{ formatDate(note.dueDate) }}
      </div>
    </div>
    
    <div class="note-actions">
      <button @click="editNote" class="edit-btn" title="Edit Note">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
          <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
        </svg>
      </button>
      
      <button @click="deleteNote" class="delete-btn" title="Delete Note">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
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
        <form @submit.prevent="updateNote" class="modal-form">
          <textarea
            v-model="editContent"
            placeholder="Enter note content..."
            required
            rows="3"
            autofocus
          ></textarea>
          <div class="modal-actions">
            <button type="button" @click="closeEditModal" class="cancel-btn">
              Cancel
            </button>
            <button type="submit" class="save-btn" :disabled="!editContent.trim()">
              Save
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

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
      editContent: ''
    }
  },
  methods: {
    async toggleDone() {
      try {
        await axios.patch(`${import.meta.env.VITE_API_BASE_URL}/notes/${this.note.id}`, {
          done: !this.note.done
        })
        this.$emit('updated')
      } catch (error) {
        console.error('Error updating note:', error)
        alert('Failed to update note. Please try again.')
      }
    },
    
    editNote() {
      this.editContent = this.note.content
      this.showEditModal = true
    },
    
    closeEditModal() {
      this.showEditModal = false
      this.editContent = ''
    },
    
    async updateNote() {
      if (!this.editContent.trim()) return
      
      try {
        await axios.patch(`${import.meta.env.VITE_API_BASE_URL}/notes/${this.note.id}`, {
          content: this.editContent.trim()
        })
        this.closeEditModal()
        this.$emit('updated')
      } catch (error) {
        console.error('Error updating note:', error)
        alert('Failed to update note. Please try again.')
      }
    },
    
    async deleteNote() {
      if (!confirm('Are you sure you want to delete this note?')) {
        return
      }
      
      try {
        await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/notes/${this.note.id}`)
        this.$emit('deleted')
      } catch (error) {
        console.error('Error deleting note:', error)
        alert('Failed to delete note. Please try again.')
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
  cursor: pointer;
}

.note-card:hover {
  border-color: #cbd5e0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.note-card.done {
  background: #f7fafc;
  border-color: #a0aec0;
}

.note-content {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
  min-width: 0;
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
</style> 