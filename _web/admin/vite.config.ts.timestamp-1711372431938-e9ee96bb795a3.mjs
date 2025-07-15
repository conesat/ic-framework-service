// vite.config.ts
import path from "node:path";
import vue from "file:///F:/projects/ic-framework-serveice/web/admin/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import vueJsx from "file:///F:/projects/ic-framework-serveice/web/admin/node_modules/@vitejs/plugin-vue-jsx/dist/index.mjs";
import { loadEnv } from "file:///F:/projects/ic-framework-serveice/web/admin/node_modules/vite/dist/node/index.js";
import { viteMockServe } from "file:///F:/projects/ic-framework-serveice/web/admin/node_modules/vite-plugin-mock/dist/index.mjs";
import svgLoader from "file:///F:/projects/ic-framework-serveice/web/admin/node_modules/vite-svg-loader/index.js";
var __vite_injected_original_dirname = "F:\\projects\\ic-framework-serveice\\web\\admin";
var CWD = process.cwd();
var vite_config_default = ({ mode }) => {
  const { VITE_BASE_URL, VITE_API_URL_PREFIX } = loadEnv(mode, CWD);
  return {
    base: VITE_BASE_URL,
    resolve: {
      alias: {
        "@": path.resolve(__vite_injected_original_dirname, "./src")
      }
    },
    css: {
      preprocessorOptions: {
        less: {
          modifyVars: {
            hack: `true; @import (reference) "${path.resolve("src/style/variables.less")}";`
          },
          math: "strict",
          javascriptEnabled: true
        }
      }
    },
    plugins: [
      vue(),
      vueJsx(),
      viteMockServe({
        mockPath: "mock",
        enable: true
      }),
      svgLoader()
    ],
    server: {
      port: 3002,
      host: "0.0.0.0",
      proxy: {
        [VITE_API_URL_PREFIX]: "http://localhost:9999/"
      }
    }
  };
};
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJGOlxcXFxwcm9qZWN0c1xcXFxpYy1mcmFtZXdvcmstc2VydmVpY2VcXFxcd2ViXFxcXGFkbWluXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJGOlxcXFxwcm9qZWN0c1xcXFxpYy1mcmFtZXdvcmstc2VydmVpY2VcXFxcd2ViXFxcXGFkbWluXFxcXHZpdGUuY29uZmlnLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9GOi9wcm9qZWN0cy9pYy1mcmFtZXdvcmstc2VydmVpY2Uvd2ViL2FkbWluL3ZpdGUuY29uZmlnLnRzXCI7aW1wb3J0IHBhdGggZnJvbSAnbm9kZTpwYXRoJztcblxuaW1wb3J0IHZ1ZSBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUnO1xuaW1wb3J0IHZ1ZUpzeCBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUtanN4JztcbmltcG9ydCB7IENvbmZpZ0VudiwgbG9hZEVudiwgVXNlckNvbmZpZyB9IGZyb20gJ3ZpdGUnO1xuaW1wb3J0IHsgdml0ZU1vY2tTZXJ2ZSB9IGZyb20gJ3ZpdGUtcGx1Z2luLW1vY2snO1xuaW1wb3J0IHN2Z0xvYWRlciBmcm9tICd2aXRlLXN2Zy1sb2FkZXInO1xuXG5jb25zdCBDV0QgPSBwcm9jZXNzLmN3ZCgpO1xuXG4vLyBodHRwczovL3ZpdGVqcy5kZXYvY29uZmlnL1xuZXhwb3J0IGRlZmF1bHQgKHsgbW9kZSB9OiBDb25maWdFbnYpOiBVc2VyQ29uZmlnID0+IHtcbiAgY29uc3QgeyBWSVRFX0JBU0VfVVJMLCBWSVRFX0FQSV9VUkxfUFJFRklYIH0gPSBsb2FkRW52KG1vZGUsIENXRCk7XG4gIHJldHVybiB7XG4gICAgYmFzZTogVklURV9CQVNFX1VSTCxcbiAgICByZXNvbHZlOiB7XG4gICAgICBhbGlhczoge1xuICAgICAgICAnQCc6IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICcuL3NyYycpLFxuICAgICAgfSxcbiAgICB9LFxuXG4gICAgY3NzOiB7XG4gICAgICBwcmVwcm9jZXNzb3JPcHRpb25zOiB7XG4gICAgICAgIGxlc3M6IHtcbiAgICAgICAgICBtb2RpZnlWYXJzOiB7XG4gICAgICAgICAgICBoYWNrOiBgdHJ1ZTsgQGltcG9ydCAocmVmZXJlbmNlKSBcIiR7cGF0aC5yZXNvbHZlKCdzcmMvc3R5bGUvdmFyaWFibGVzLmxlc3MnKX1cIjtgLFxuICAgICAgICAgIH0sXG4gICAgICAgICAgbWF0aDogJ3N0cmljdCcsXG4gICAgICAgICAgamF2YXNjcmlwdEVuYWJsZWQ6IHRydWUsXG4gICAgICAgIH0sXG4gICAgICB9LFxuICAgIH0sXG5cbiAgICBwbHVnaW5zOiBbXG4gICAgICB2dWUoKSxcbiAgICAgIHZ1ZUpzeCgpLFxuICAgICAgdml0ZU1vY2tTZXJ2ZSh7XG4gICAgICAgIG1vY2tQYXRoOiAnbW9jaycsXG4gICAgICAgIGVuYWJsZTogdHJ1ZSxcbiAgICAgIH0pLFxuICAgICAgc3ZnTG9hZGVyKCksXG4gICAgXSxcblxuICAgIHNlcnZlcjoge1xuICAgICAgcG9ydDogMzAwMixcbiAgICAgIGhvc3Q6ICcwLjAuMC4wJyxcbiAgICAgIHByb3h5OiB7XG4gICAgICAgIFtWSVRFX0FQSV9VUkxfUFJFRklYXTogJ2h0dHA6Ly9sb2NhbGhvc3Q6OTk5OS8nLFxuICAgICAgfSxcbiAgICB9LFxuICB9O1xufTtcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFBNlQsT0FBTyxVQUFVO0FBRTlVLE9BQU8sU0FBUztBQUNoQixPQUFPLFlBQVk7QUFDbkIsU0FBb0IsZUFBMkI7QUFDL0MsU0FBUyxxQkFBcUI7QUFDOUIsT0FBTyxlQUFlO0FBTnRCLElBQU0sbUNBQW1DO0FBUXpDLElBQU0sTUFBTSxRQUFRLElBQUk7QUFHeEIsSUFBTyxzQkFBUSxDQUFDLEVBQUUsS0FBSyxNQUE2QjtBQUNsRCxRQUFNLEVBQUUsZUFBZSxvQkFBb0IsSUFBSSxRQUFRLE1BQU0sR0FBRztBQUNoRSxTQUFPO0FBQUEsSUFDTCxNQUFNO0FBQUEsSUFDTixTQUFTO0FBQUEsTUFDUCxPQUFPO0FBQUEsUUFDTCxLQUFLLEtBQUssUUFBUSxrQ0FBVyxPQUFPO0FBQUEsTUFDdEM7QUFBQSxJQUNGO0FBQUEsSUFFQSxLQUFLO0FBQUEsTUFDSCxxQkFBcUI7QUFBQSxRQUNuQixNQUFNO0FBQUEsVUFDSixZQUFZO0FBQUEsWUFDVixNQUFNLDhCQUE4QixLQUFLLFFBQVEsMEJBQTBCLENBQUM7QUFBQSxVQUM5RTtBQUFBLFVBQ0EsTUFBTTtBQUFBLFVBQ04sbUJBQW1CO0FBQUEsUUFDckI7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLElBRUEsU0FBUztBQUFBLE1BQ1AsSUFBSTtBQUFBLE1BQ0osT0FBTztBQUFBLE1BQ1AsY0FBYztBQUFBLFFBQ1osVUFBVTtBQUFBLFFBQ1YsUUFBUTtBQUFBLE1BQ1YsQ0FBQztBQUFBLE1BQ0QsVUFBVTtBQUFBLElBQ1o7QUFBQSxJQUVBLFFBQVE7QUFBQSxNQUNOLE1BQU07QUFBQSxNQUNOLE1BQU07QUFBQSxNQUNOLE9BQU87QUFBQSxRQUNMLENBQUMsbUJBQW1CLEdBQUc7QUFBQSxNQUN6QjtBQUFBLElBQ0Y7QUFBQSxFQUNGO0FBQ0Y7IiwKICAibmFtZXMiOiBbXQp9Cg==
