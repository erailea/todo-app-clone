import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig(({ command, mode }) => {
  // Load env file based on `mode` in the current working directory.
  const env = loadEnv(mode, process.cwd(), '')
  
  return {
    plugins: [vue()],
    server: {
      port: parseInt(env.VITE_DEV_SERVER_PORT) || 3000,
      host: env.VITE_DEV_SERVER_HOST === 'true' || true
    },
    build: {
      target: env.VITE_BUILD_TARGET || 'esnext',
      outDir: env.VITE_BUILD_OUTDIR || 'dist'
    },
    define: {
      __API_BASE_URL__: JSON.stringify(env.VITE_API_BASE_URL || 'http://localhost:8080')
    }
  }
}) 