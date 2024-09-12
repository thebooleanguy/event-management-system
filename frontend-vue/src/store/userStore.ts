// store/userStore.ts
import { createStore } from "vuex";

interface UserState {
    user: any; // Define a proper type based on your user object structure
}

const store = createStore<UserState>({
    state: {
        user: null,
    },
    mutations: {
        LOGIN(state, user) {
            state.user = user;
        },
        LOGOUT(state) {
            state.user = null;
        },
        UPDATE_USER(state, data) {
            if (state.user) {
                state.user = { ...state.user, ...data };
            }
        },
    },
    actions: {
        login({ commit }, user) {
            commit("LOGIN", user);
        },
        logout({ commit }) {
            commit("LOGOUT");
        },
        updateUser({ commit }, data) {
            commit("UPDATE_USER", data);
        },
    },
    getters: {
        getUser(state) {
            return state.user;
        },
    },
});

export default store;
