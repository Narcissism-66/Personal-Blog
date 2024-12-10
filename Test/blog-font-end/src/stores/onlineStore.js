import { defineStore } from "pinia";

export const UserOnline = defineStore('online', {
    state: () => ({
        online: 0,
    }),
    actions: {
        AddOnline() {
            this.online += 1;
        },
        DecreaseOnline() {
            this.online -= 1;
        },
        SetOnline(count) {
            this.online = count;
        }
    },
    getters: {
        getOnline: (state) => state.online,
    }
});