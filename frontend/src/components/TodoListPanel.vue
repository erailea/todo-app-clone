<template>
  <div class="todo-list-panel">
    <div class="panel-header">
      <h3 class="list-title">{{ list.title }}</h3>
      <div class="panel-actions">
        <button @click="showAddNote = true" class="add-note-btn" title="Add Note">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
        </button>
        <button @click="deleteList" class="delete-btn" title="Delete List">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="3,6 5,6 21,6"/>
            <path d="M19,6V20a2,2 0 0,1 -2,2H7a2,2 0 0,1 -2,-2V6M8,6V4a2,2 0 0,1 2,-2h4a2,2 0 0,1 2,2V6"/>
          </svg>
        </button>
      </div>
    </div>

    <div class="notes-container">
      <div v-if="loading" class="loading">
        Loading notes...
      </div>
      
      <div v-else-if="notes.length === 0" class="empty-notes">
        <p>No notes in this list</p>
        <button @click="showAddNote = true" class="add-first-note-btn">
          Add your first note
        </button>
      </div>
      
      <div v-else class="notes-list">
        <NoteCard
          v-for="note in notes"
          :key="note.id"
          :note="note"
          @updated="loadNotes"
          @deleted="loadNotes"
        />
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
        <div class="form-actions">
          <button type="button" @click="cancelAddNote" class="cancel-btn">Cancel</button>
          <button type="submit" class="add-btn" :disabled="!newNoteContent.trim()">Add</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import NoteCard from './NoteCard.vue'

export default {
  name: 'TodoListPanel',
  components: {
    NoteCard
  },
  props: {
    list: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      notes: [],
      loading: false,
      showAddNote: false,
      newNoteContent: ''
    }
  },
  methods: {
    async loadNotes() {
      this.loading = true
      try {
        const response = await axios.get(`http://localhost:8080/lists/${this.list.id}/notes`)
        this.notes = response.data
      } catch (error) {
        console.error('Error loading notes:', error)
        this.notes = []
      } finally {
        this.loading = false
      }
    },
    
    async addNote() {
      if (!this.newNoteContent.trim()) return
      
      try {
        await axios.post(`http://localhost:8080/lists/${this.list.id}/notes`, {
          content: this.newNoteContent.trim()
        })
        
        this.newNoteContent = ''
        this.showAddNote = false
        await this.loadNotes()
      } catch (error) {
        console.error('Error adding note:', error)
        alert('Failed to add note. Please try again.')
      }
    },
    
    cancelAddNote() {
      this.showAddNote = false
      this.newNoteContent = ''
    },
    
    async deleteList() {
      if (!confirm(`Are you sure you want to delete "${this.list.title}"? This action cannot be undone.`)) {
        return
      }
      
      try {
        await axios.delete(`http://localhost:8080/lists/${this.list.id}`)
        this.$emit('refresh')
      } catch (error) {
        console.error('Error deleting list:', error)
        alert('Failed to delete list. Please try again.')
      }
    }
  },
  mounted() {
    this.loadNotes()
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

.list-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0;
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

.empty-notes {
  padding: 2rem;
  text-align: center;
  color: #718096;
}

.empty-notes p {
  margin-bottom: 1rem;
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

.notes-list {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
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
</style> 