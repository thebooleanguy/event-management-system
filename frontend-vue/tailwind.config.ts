// tailwind.config.ts
import type { Config } from "tailwindcss";

const config: Config = {
    content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
    theme: {
        extend: {
            colors: {
                // Add custom colors if needed
                "header-black": "#1A1A1A", // Adjust this hex code as needed
                "background-gray": "#E5E5E5", // Adjust this hex code as needed
                "card-white": "#FFFFFF",
                "button-yellow": "#F7D44C", // Adjust this hex code as needed
                "accent-blue": "#7DD3FC", // Adjust this hex code as needed
                "dark-blue": "#1E40AF", // Adjust this hex code as needed
            },
        },
    },
    plugins: [],
};

export default config;
