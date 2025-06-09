import { createStore } from 'vuex'
import axios from 'axios'
import { getToken, setToken, removeToken } from '../utils/auth'

// Setup axios default authorization header
const token = getToken()
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

// Helper function to sort notes by dueDate (null values last)
const sortNotesByDueDate = (notes) => {
  return notes.sort((a, b) => {
    // If both have dueDate, sort by date
    if (a.dueDate && b.dueDate) {
      return new Date(a.dueDate) - new Date(b.dueDate)
    }
    // If only a has dueDate, a comes first
    if (a.dueDate && !b.dueDate) {
      return -1
    }
    // If only b has dueDate, b comes first
    if (!a.dueDate && b.dueDate) {
      return 1
    }
    // If neither has dueDate, maintain original order by createdAt
    return new Date(a.createdAt) - new Date(b.createdAt)
  })
}

export default createStore({
  state: {
    user: null,
    isAuthenticated: !!getToken(),
    todoLists: [],
    listNotes: {} // Store notes by listId: { listId: [notes] }
  },
  
  mutations: {
    SET_USER(state, user) {
      state.user = user
      state.isAuthenticated = !!user
    },
    
    SET_TOKEN(state, token) {
      setToken(token)
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      state.isAuthenticated = true
    },
    
    LOGOUT(state) {
      state.user = null
      state.isAuthenticated = false
      state.todoLists = []
      state.listNotes = {}
      removeToken()
      delete axios.defaults.headers.common['Authorization']
    },
    
    SET_TODO_LISTS(state, todoListsResponse) {
      // todoListsResponse is now an array of {id, title, createdAt, userId, notes}
      state.todoLists = todoListsResponse.map(item => ({
        id: item.id,
        title: item.title,
        createdAt: item.createdAt,
        userId: item.userId
      }))
      state.listNotes = {}
      todoListsResponse.forEach(item => {
        // Notes are already sorted by backend, just assign them
        state.listNotes[item.id] = item.notes
      })
    },
    
    ADD_TODO_LIST(state, list) {
      state.todoLists.push(list)
      state.listNotes[list.id] = []
    },
    
    UPDATE_TODO_LIST(state, updatedList) {
      const index = state.todoLists.findIndex(list => list.id === updatedList.id)
      if (index !== -1) {
        state.todoLists.splice(index, 1, updatedList)
      }
    },
    
    DELETE_TODO_LIST(state, listId) {
      state.todoLists = state.todoLists.filter(list => list.id !== listId)
      delete state.listNotes[listId]
    },

    SET_LIST_NOTES(state, { listId, notes }) {
      // Notes are already sorted by backend
      state.listNotes[listId] = notes
    },

    ADD_NOTE_TO_LIST(state, { listId, note }) {
      if (!state.listNotes[listId]) {
        state.listNotes[listId] = []
      }
      // Add the note and re-sort to maintain dueDate order
      state.listNotes[listId].push(note)
      state.listNotes[listId] = sortNotesByDueDate(state.listNotes[listId])
    },

    UPDATE_NOTE_IN_LIST(state, { listId, note }) {
      if (state.listNotes[listId]) {
        const index = state.listNotes[listId].findIndex(n => n.id === note.id)
        if (index !== -1) {
          state.listNotes[listId].splice(index, 1, note)
          // Re-sort after update in case dueDate changed
          state.listNotes[listId] = sortNotesByDueDate(state.listNotes[listId])
        }
      }
    },

    DELETE_NOTE_FROM_LIST(state, { listId, noteId }) {
      if (state.listNotes[listId]) {
        state.listNotes[listId] = state.listNotes[listId].filter(note => note.id !== noteId)
        // No need to re-sort after deletion
      }
    }
  },
  
  actions: {
    async login({ commit }, credentials) {
      try {
        const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL}/auth/authenticate`, credentials)
        const { token } = response.data
        
        commit('SET_TOKEN', token)
        await this.dispatch('fetchUser')
        
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    async register({ commit }, userData) {
      try {
        const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL}/auth/register`, userData)
        const { token } = response.data
        
        commit('SET_TOKEN', token)
        await this.dispatch('fetchUser')
        
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    async fetchUser({ commit }) {
      try {
        const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/user/me`)
        commit('SET_USER', response.data)
        return response.data
      } catch (error) {
        commit('LOGOUT')
        throw error
      }
    },
    
    async fetchTodoLists({ commit }) {
      try {
        const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/lists`)
        commit('SET_TODO_LISTS', response.data)
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    async createTodoList({ commit }, title) {
      try {
        const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL}/lists`, { title })
        commit('ADD_TODO_LIST', response.data)
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    async updateTodoList({ commit }, { id, title }) {
      try {
        const response = await axios.patch(`${import.meta.env.VITE_API_BASE_URL}/lists/${id}`, { title })
        commit('UPDATE_TODO_LIST', response.data)
        return response.data
      } catch (error) {
        throw error
      }
    },

    async fetchNotes({ commit }, listId) {
      try {
        const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/lists/${listId}/notes`)
        commit('SET_LIST_NOTES', { listId, notes: response.data })
        return response.data
      } catch (error) {
        throw error
      }
    },

    async createNote({ commit }, { listId, noteData }) {
      try {
        const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL}/lists/${listId}/notes`, noteData)
        commit('ADD_NOTE_TO_LIST', { listId, note: response.data })
        return response.data
      } catch (error) {
        throw error
      }
    },

    async updateNote({ commit }, { noteId, noteData }) {
      try {
        const response = await axios.patch(`${import.meta.env.VITE_API_BASE_URL}/notes/${noteId}`, noteData)
        // Find the list this note belongs to and update it
        const updatedNote = response.data
        commit('UPDATE_NOTE_IN_LIST', { listId: updatedNote.listId, note: updatedNote })
        return response.data
      } catch (error) {
        throw error
      }
    },

    async deleteNote({ commit, state }, { noteId, listId }) {
      try {
        await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/notes/${noteId}`)
        commit('DELETE_NOTE_FROM_LIST', { listId, noteId })
      } catch (error) {
        throw error
      }
    },
    
    logout({ commit }) {
      commit('LOGOUT')
    }
  },
  
  getters: {
    isAuthenticated: state => state.isAuthenticated,
    user: state => state.user,
    todoLists: state => state.todoLists,
    getListNotes: (state) => (listId) => state.listNotes[listId] || []
  }
}) 