import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import postcssImport from "postcss-import";
import tailwindcss from "tailwindcss";
import autoprefixer from "autoprefixer";
import { resolve } from "path";

// https://vitejs.dev/config/
export default defineConfig({
    build: {
        sourcemap: true,
    },
    plugins: [vue()],
    resolve: {
        alias: {
            "@": resolve(__dirname, "src"), // Alias for @ to src
        },
    },
    css: {
        postcss: {
            plugins: [postcssImport(), tailwindcss(), autoprefixer()],
        },
    },
});
