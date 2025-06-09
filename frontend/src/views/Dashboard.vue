<template>
  <div class="dashboard">
    <!-- Navbar -->
    <nav class="navbar">
      <div class="navbar-content">
        <h1 class="app-title">Todo App</h1>
        <div class="user-section">
          <span class="user-name">{{ user?.fullName || 'User' }}</span>
          <button @click="handleLogout" class="logout-btn" title="Logout">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
              <polyline points="16,17 21,12 16,7"/>
              <line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
          </button>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <main class="main-content">
      <div class="content-container">
        <!-- Header -->
        <div class="page-header">
          <h2>Your Todo Lists</h2>
          <button @click="showCreateModal = true" class="create-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="12" y1="5" x2="12" y2="19"/>
              <line x1="5" y1="12" x2="19" y2="12"/>
            </svg>
            New List
          </button>
        </div>

        <!-- Todo Lists -->
        <div class="lists-container">
          <div v-if="loading" class="loading">
            Loading your lists...
          </div>
          
          <div v-else-if="todoLists.length === 0" class="empty-state">
            <p>No todo lists yet. Create your first list!</p>
          </div>
          
          <div v-else class="lists-grid">
            <TodoListPanel
              v-for="list in todoLists"
              :key="list.id"
              :list="list"
              @refresh="loadTodoLists"
            />
          </div>
        </div>
      </div>
    </main>

    <!-- Create List Modal -->
    <div v-if="showCreateModal" class="modal-overlay" @click="showCreateModal = false">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>Create New List</h3>
          <button @click="showCreateModal = false" class="close-btn">×</button>
        </div>
        <form @submit.prevent="createList" class="modal-form">
          <input
            v-model="newListTitle"
            type="text"
            placeholder="Enter list title"
            required
            autofocus
          />
          <div class="modal-actions">
            <button type="button" @click="showCreateModal = false" class="cancel-btn" :disabled="createLoading">
              Cancel
            </button>
            <button type="submit" class="submit-btn" :disabled="!newListTitle.trim() || createLoading">
              <div v-if="createLoading" class="loading-spinner"></div>
              <span v-else>Create</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import TodoListPanel from '../components/TodoListPanel.vue'

export default {
  name: 'Dashboard',
  components: {
    TodoListPanel
  },
  data() {
    return {
      loading: true,
      createLoading: false,
      showCreateModal: false,
      newListTitle: ''
    }
  },
  computed: {
    ...mapGetters(['user', 'todoLists'])
  },
  methods: {
    ...mapActions(['logout', 'fetchUser', 'fetchTodoLists', 'createTodoList']),
    
    async loadTodoLists() {
      this.loading = true
      try {
        await this.fetchTodoLists()
      } catch (error) {
        console.error('Error loading todo lists:', error)
      } finally {
        this.loading = false
      }
    },
    
    async createList() {
      if (!this.newListTitle.trim() || this.createLoading) return
      
      this.createLoading = true
      try {
        await this.createTodoList(this.newListTitle.trim())
        this.showCreateModal = false
        this.newListTitle = ''
      } catch (error) {
        console.error('Error creating list:', error)
        alert('Failed to create list. Please try again.')
      } finally {
        this.createLoading = false
      }
    },
    
    handleLogout() {
      this.logout()
      this.$router.push('/login')
    }
  },
  async mounted() {
    try {
      if (!this.user) {
        await this.fetchUser()
      }
      await this.loadTodoLists()
    } catch (error) {
      console.error('Error initializing dashboard:', error)
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background-color: #f8fafc;
}

.navbar {
  background: white;
  border-bottom: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
}

.navbar-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1a202c;
}

.user-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-name {
  font-weight: 500;
  color: #374151;
}

.logout-btn {
  padding: 0.5rem;
  background: #f7fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #718096;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logout-btn:hover {
  background: #edf2f7;
  color: #4a5568;
}

.main-content {
  padding: 2rem 0;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-header h2 {
  font-size: 1.875rem;
  font-weight: 700;
  color: #1a202c;
}

.create-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.create-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 10px 15px -3px rgba(102, 126, 234, 0.3);
}

.lists-container {
  min-height: 400px;
}

.loading, .empty-state {
  text-align: center;
  padding: 3rem;
  color: #718096;
}

.lists-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 1.5rem;
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
  font-size: 1.25rem;
  font-weight: 600;
  color: #1a202c;
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

.modal-form input {
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 1rem;
}

.modal-form input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.modal-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: flex-end;
}

.cancel-btn, .submit-btn {
  padding: 0.5rem 1rem;
  border-radius: 8px;
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

.submit-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .content-container {
    padding: 0 1rem;
  }
  
  .navbar-content {
    padding: 1rem;
  }
  
  .page-header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }
  
  .lists-grid {
    grid-template-columns: 1fr;
  }
}
</style> 