import { mergeConfig, loadEnv } from 'vite';
import eslint from 'vite-plugin-eslint';
import baseConfig from './vite.config.base';

export default mergeConfig(
  {
    mode: 'development',
    server: {
      fs: {
        strict: true,
      },

        proxy: {
            '/api': {
                target: loadEnv('development', process.cwd()).VITE_API_BASE_URL,
                changeOrigin: true,
                rewrite: (path) => path.replace(new RegExp(`^/api`), ''),
            },
            '/cpi': {
                target: loadEnv('development', process.cwd()).VITE_API_BASE_URL,
                changeOrigin: true,
                rewrite: (path) => path.replace(new RegExp(`^/cpi`), ''),
            },
        },
    },
    plugins: [
      eslint({
        cache: false,
        include: ['src/**/*.ts', 'src/**/*.tsx', 'src/**/*.vue'],
        exclude: ['node_modules'],
      }),
    ],
  },
  baseConfig
);
