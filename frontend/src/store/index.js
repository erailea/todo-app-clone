import { createStore } from 'vuex'
import axios from 'axios'
import { getToken, setToken, removeToken } from '../utils/auth'

// Get API base URL from runtime config or fallback to environment variable
const API_BASE_URL = window.__APP_CONFIG__?.API_BASE_URL || import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// Setup axios default authorization header
const token = getToken()
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

export default createStore({
  state: {
    user: null,
    isAuthenticated: !!getToken(),
    todoLists: []
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
      removeToken()
      delete axios.defaults.headers.common['Authorization']
    },
    
    SET_TODO_LISTS(state, lists) {
      state.todoLists = lists
    },
    
    ADD_TODO_LIST(state, list) {
      state.todoLists.push(list)
    },
    
    UPDATE_TODO_LIST(state, updatedList) {
      const index = state.todoLists.findIndex(list => list.id === updatedList.id)
      if (index !== -1) {
        state.todoLists.splice(index, 1, updatedList)
      }
    },
    
    DELETE_TODO_LIST(state, listId) {
      state.todoLists = state.todoLists.filter(list => list.id !== listId)
    }
  },
  
  actions: {
    async login({ commit }, credentials) {
      try {
        const response = await axios.post(`${API_BASE_URL}/auth/authenticate`, credentials)
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
        const response = await axios.post(`${API_BASE_URL}/auth/register`, userData)
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
        const response = await axios.get(`${API_BASE_URL}/user/me`)
        commit('SET_USER', response.data)
        return response.data
      } catch (error) {
        commit('LOGOUT')
        throw error
      }
    },
    
    async fetchTodoLists({ commit }) {
      try {
        const response = await axios.get(`${API_BASE_URL}/lists`)
        commit('SET_TODO_LISTS', response.data)
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    async createTodoList({ commit }, title) {
      try {
        const response = await axios.post(`${API_BASE_URL}/lists`, { title })
        commit('ADD_TODO_LIST', response.data)
        return response.data
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
    todoLists: state => state.todoLists
  }
}) 