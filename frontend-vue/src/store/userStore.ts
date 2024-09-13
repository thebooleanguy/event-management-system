import { createStore } from "vuex";

interface User {
    id: string;
    email: string;
    name?: string;
    role?: string; // Add a role property to identify user roles
}

interface UserState {
    user: User | null; // user can be null if not logged in
}

enum MutationTypes {
    LOGIN = "LOGIN",
    LOGOUT = "LOGOUT",
    UPDATE_USER = "UPDATE_USER",
}

const store = createStore<UserState>({
    state: {
        user: null,
    },

    mutations: {
        [MutationTypes.LOGIN](state, user: User) {
            state.user = user;
        },
        [MutationTypes.LOGOUT](state) {
            state.user = null;
        },
        [MutationTypes.UPDATE_USER](state, data: Partial<User>) {
            if (state.user) {
                state.user = { ...state.user, ...data };
            }
        },
    },

    actions: {
        async login({ commit }: { commit: Function }, user: User) {
            commit(MutationTypes.LOGIN, user);
        },
        async logout({ commit }: { commit: Function }) {
            commit(MutationTypes.LOGOUT);
        },
        async updateUser(
            { commit }: { commit: Function },
            data: Partial<User>
        ) {
            commit(MutationTypes.UPDATE_USER, data);
        },
    },

    getters: {
        getUser(state) {
            return state.user;
        },
        isAdmin(state) {
            // Check if the user exists and if their role is 'ADMIN'
            return state.user?.role === "ADMIN";
        },
    },
});

export default store;
