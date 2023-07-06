import { defineConfig } from 'vite'
import { join } from "path";
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  // 路径别名
  resolve: {
    alias: {
      '@': join(__dirname, 'src'),
    }
  },
  // 代理
  server: {
    proxy: {
      '/api': {
        target: 'http://124.71.208.71:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
