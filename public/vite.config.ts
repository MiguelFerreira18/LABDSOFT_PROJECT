/// <reference types="vitest" />

import legacy from '@vitejs/plugin-legacy';
import vue from '@vitejs/plugin-vue';
import path from 'path';
import dotenv from 'dotenv';
import { defineConfig } from 'vite';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const envFiles = {
    development: '.env.development',
    production: '.env.production',
  };

  const envFile = dotenv.config({ path: envFiles[mode] }).parsed;

  console.log(envFile);
  return {
    plugins: [vue(), legacy()],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
    test: {
      globals: true,
      environment: 'jsdom',
    },
  };
});
