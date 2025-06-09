<template>
  <div class="todo-list-panel">
    <div class="panel-header">
      <div v-if="!editingTitle" class="list-title-container">
        <h3 class="list-title" @click="startEditTitle">{{ list.title }}</h3>
        <button @click="startEditTitle" class="edit-title-btn" title="Edit Title">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
          </svg>
        </button>
      </div>
      <div v-else class="title-edit-container">
        <input
          v-model="editTitleValue"
          @blur="saveTitle"
          @keyup.enter="saveTitle"
          @keyup.escape="cancelEditTitle"
          class="title-input"
          autofocus
        />
        <div class="title-edit-actions">
          <button @click="saveTitle" class="save-title-btn" title="Save" :disabled="loading.updateTitle">
            <div v-if="loading.updateTitle" class="loading-spinner small"></div>
            <span v-else>✓</span>
          </button>
          <button @click="cancelEditTitle" class="cancel-title-btn" title="Cancel" :disabled="loading.updateTitle">✕</button>
        </div>
      </div>
      <div class="panel-actions">
        <button @click="showAddNote = true" class="add-note-btn" title="Add Note" :disabled="anyLoading">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
        </button>
        <button @click="deleteList" class="delete-btn" title="Delete List" :disabled="anyLoading">
          <div v-if="loading.deleteList" class="loading-spinner"></div>
          <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="3,6 5,6 21,6"/>
            <path d="M19,6V20a2,2 0 0,1 -2,2H7a2,2 0 0,1 -2,-2V6M8,6V4a2,2 0 0,1 2,-2h4a2,2 0 0,1 2,2V6"/>
          </svg>
        </button>
      </div>
    </div>

    <div class="notes-container">
      <div v-if="loading.notes" class="loading">
        Loading notes...
      </div>
      
      <div v-else class="notes-list">
        <draggable 
          v-model="dragNotes" 
          :group="{ name: 'notes', pull: true, put: true }"
          :disabled="editingTitle || modalOpen"
          handle=".drag-handle"
          @start="onDragStart"
          @end="onDragEnd"
          item-key="id"
          class="draggable-container"
          :class="{ 'empty-list': notes.length === 0 }"
        >
          <template #item="{ element }">
            <NoteCard
              :key="element.id"
              :note="element"
              @updated="handleNoteUpdated"
              @deleted="handleNoteDeleted"
              @modal-opened="modalOpen = true"
              @modal-closed="modalOpen = false"
            />
          </template>
          
          <!-- Empty state that still accepts drops -->
          <div v-if="notes.length === 0" class="empty-drop-zone">
            <p>No notes in this list</p>
            <button @click="showAddNote = true" class="add-first-note-btn">
              Add your first note
            </button>
          </div>
        </draggable>
      </div>
    </div>

    <!-- Add Note Form -->
    <div v-if="showAddNote" class="add-note-form">
      <form @submit.prevent="addNote">
        <input
          v-model="newNoteContent"
          type="text"
          placeholder="Enter note content..."
          required
          autofocus
        />
        <input
          v-model="newNoteDueDate"
          type="date"
          class="date-input"
          placeholder="Due date (optional)"
        />
        <div class="form-actions">
          <button type="button" @click="cancelAddNote" class="cancel-btn" :disabled="loading.addNote">Cancel</button>
          <button type="submit" class="add-btn" :disabled="!newNoteContent.trim() || loading.addNote">
            <div v-if="loading.addNote" class="loading-spinner small"></div>
            <span v-else>Add</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import axios from 'axios'
import draggable from 'vuedraggable'
import NoteCard from './NoteCard.vue'
import { handleApiError } from '../utils/error'

export default {
  name: 'TodoListPanel',
  components: {
    NoteCard,
    draggable
  },
  props: {
    list: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      loading: {
        notes: false,
        addNote: false,
        updateTitle: false,
        deleteList: false
      },
      showAddNote: false,
      newNoteContent: '',
      newNoteDueDate: '',
      editingTitle: false,
      editTitleValue: '',
      draggedNote: null,
      modalOpen: false
    }
  },
  computed: {
    ...mapGetters(['getListNotes']),
    notes() {
      return this.getListNotes(this.list.id)
    },
    dragNotes: {
      get() {
        return this.notes
      },
      set(value) {
        // This will be handled by drag events
      }
    },
    anyLoading() {
      return this.loading.notes || this.loading.addNote || this.loading.updateTitle || this.loading.deleteList
    }
  },
  methods: {
    ...mapActions(['createNote', 'updateTodoList', 'fetchNotes', 'moveNote']),
    
    onDragStart(evt) {
      this.draggedNote = this.notes[evt.oldIndex]
    },
    
    async onDragEnd(evt) {
      // Check if note was moved to a different list
      if (evt.from !== evt.to) {
        const note = this.draggedNote
        const fromListId = this.list.id
        
        // Find the target list ID from the parent element
        const toElement = evt.to.closest('.todo-list-panel')
        const toListId = toElement?.dataset?.listId
        
        if (toListId && toListId !== fromListId) {
          try {
            await this.moveNote({
              noteId: note.id,
              fromListId,
              toListId,
              note
            })
            
            // Show success message (optional)
            this.$emit('note-moved', { 
              note, 
              fromListId, 
              toListId,
              success: true 
            })
          } catch (error) {
            console.error('Error moving note:', error)
            
            // Show error message
            alert(handleApiError(error))
            
            this.$emit('note-moved', { 
              note, 
              fromListId, 
              toListId,
              success: false,
              error 
            })
          }
        }
      }
      
      this.draggedNote = null
    },
    
    async addNote() {
      if (!this.newNoteContent.trim() || this.loading.addNote) return
      
      this.loading.addNote = true
      try {
        await this.createNote({
          listId: this.list.id,
          noteData: {
            content: this.newNoteContent.trim(),
            dueDate: this.newNoteDueDate ? new Date(this.newNoteDueDate).toISOString() : null
          }
        })
        
        this.newNoteContent = ''
        this.newNoteDueDate = ''
        this.showAddNote = false
      } catch (error) {
        console.error('Error adding note:', error)
        alert(handleApiError(error))
      } finally {
        this.loading.addNote = false
      }
    },
    
    cancelAddNote() {
      if (this.loading.addNote) return
      
      this.showAddNote = false
      this.newNoteContent = ''
      this.newNoteDueDate = ''
    },
    
    async deleteList() {
      if (this.loading.deleteList) return
      
      if (!confirm(`Are you sure you want to delete "${this.list.title}"? This action cannot be undone.`)) {
        return
      }
      
      this.loading.deleteList = true
      try {
        await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/lists/${this.list.id}`)
        this.$emit('refresh')
      } catch (error) {
        console.error('Error deleting list:', error)
        alert(handleApiError(error))
      } finally {
        this.loading.deleteList = false
      }
    },
    
    startEditTitle() {
      if (this.anyLoading) return
      
      this.editingTitle = true
      this.editTitleValue = this.list.title
    },
    
    async saveTitle() {
      if (!this.editTitleValue.trim() || this.loading.updateTitle) {
        this.cancelEditTitle()
        return
      }
      
      this.loading.updateTitle = true
      try {
        await this.updateTodoList({
          id: this.list.id,
          title: this.editTitleValue.trim()
        })
        this.editingTitle = false
      } catch (error) {
        console.error('Error updating list title:', error)
        alert(handleApiError(error))
        this.cancelEditTitle()
      } finally {
        this.loading.updateTitle = false
      }
    },
    
    cancelEditTitle() {
      this.editingTitle = false
      this.loading.updateTitle = false
    },

    handleNoteUpdated() {
      // Note is already updated via store actions in NoteCard
      // No need to refresh anything
    },

    handleNoteDeleted() {
      // Note is already deleted via store actions in NoteCard  
      // No need to refresh anything
    }
  },
  created() {
    // Notes are already loaded from the initial fetch
    // No need to load them again unless they're missing
    if (this.notes.length === 0) {
      this.fetchNotes(this.list.id)
    }
  },
  mounted() {
    // Add data attribute for identifying the list during drag operations
    this.$el.dataset.listId = this.list.id
  }
}
</script>

<style scoped>
.todo-list-panel {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  overflow: hidden;
  height: fit-content;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
}

.panel-header {
  padding: 1.5rem;
  border-bottom: 1px solid #f1f5f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.list-title-container {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.list-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0;
}

.edit-title-btn {
  padding: 0.5rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f9ff;
  color: #0369a1;
}

.edit-title-btn:hover {
  background: #e0f2fe;
  color: #0c4a6e;
}

.title-edit-container {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.title-input {
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 0.875rem;
}

.title-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.title-edit-actions {
  display: flex;
  gap: 0.5rem;
}

.save-title-btn, .cancel-title-btn {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.save-title-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
}

.save-title-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.cancel-title-btn {
  background: #f7fafc;
  border: 1px solid #e2e8f0;
  color: #718096;
}

.cancel-title-btn:hover {
  background: #edf2f7;
}

.panel-actions {
  display: flex;
  gap: 0.5rem;
}

.add-note-btn, .delete-btn {
  padding: 0.5rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-note-btn {
  background: #f0f9ff;
  color: #0369a1;
}

.add-note-btn:hover {
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

.notes-container {
  flex: 1;
  overflow-y: auto;
  min-height: 200px;
  max-height: 60vh;
}

.loading {
  padding: 2rem;
  text-align: center;
  color: #718096;
}

.notes-list {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  min-height: 150px;
}

.draggable-container {
  min-height: 100px;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  transition: all 0.2s ease;
}

.draggable-container.empty-list {
  min-height: 150px;
  border: 2px dashed #e2e8f0;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafbfc;
}

.empty-drop-zone {
  text-align: center;
  color: #718096;
  padding: 2rem;
}

.empty-drop-zone p {
  margin-bottom: 1rem;
  font-size: 0.875rem;
}

.add-first-note-btn {
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s;
}

.add-first-note-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

/* Drag and drop styles */
.sortable-ghost {
  opacity: 0.5;
  background: #f0f9ff;
  border: 2px dashed #60a5fa;
  border-radius: 12px;
}

.sortable-chosen {
  transform: rotate(2deg);
  box-shadow: 0 8px 25px -8px rgba(0, 0, 0, 0.3);
}

.sortable-drag {
  opacity: 0.8;
  transform: rotate(5deg);
  cursor: grabbing !important;
}

/* Drop zone styling */
.draggable-container:not(.empty-list):hover {
  background: rgba(96, 165, 250, 0.05);
  border-radius: 8px;
  transition: background 0.2s ease;
}

.draggable-container.empty-list.sortable-drag-over {
  background: rgba(96, 165, 250, 0.1);
  border-color: #60a5fa;
  border-style: solid;
}

/* Disable drag when editing */
.todo-list-panel .draggable-container[disabled] {
  pointer-events: none;
}

.todo-list-panel .draggable-container[disabled] .note-card {
  pointer-events: auto;
}

.add-note-form {
  padding: 1rem;
  border-top: 1px solid #f1f5f9;
  background: #fafbfc;
}

.add-note-form form {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.add-note-form input {
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 0.875rem;
}

.add-note-form input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.date-input {
  margin-top: 0.5rem;
  color: #374151;
}

.form-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.cancel-btn, .add-btn {
  padding: 0.5rem 1rem;
  border-radius: 6px;
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

.add-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
}

.add-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.add-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
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

.add-note-btn:disabled,
.delete-btn:disabled,
.save-title-btn:disabled,
.cancel-title-btn:disabled,
.add-btn:disabled,
.cancel-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 