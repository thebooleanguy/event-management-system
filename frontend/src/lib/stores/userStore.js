import { writable } from 'svelte/store';

function createUserStore() {
    const { subscribe, set, update } = writable(null);

    return {
        subscribe,
        login: (user) => set(user),
        logout: () => set(null),
        update: (data) => update(user => ({ ...user, ...data }))
    };
}

export const user = createUserStore();
