import { createRouter, createWebHistory } from "vue-router";
import Home from "@/views/HomeView.vue"
import MemberCreate from "@/views/MemberCreate.vue"
import LoginPage from "@/views/LoginPage.vue"
import MemberList from "@/views/MemberList.vue"
import GroupChatList from "@/views/GroupChatList.vue"
import SimpleWebsocket from "@/views/SimpleWebsocket.vue"
import StompChatPage from "@/views/StompChatPage.vue"

const routes = [
    { path: '/', name: 'Home', component: Home },
    { path: '/member/create', name: 'MemberCreate', component: MemberCreate },
    { path: '/login', name: 'LoginPage', component: LoginPage },
    { path: '/member/list', name: 'MemberList', component: MemberList },
    { path: '/groupchatting/list', name: 'GroupChatList', component: GroupChatList },
    { path: '/simple/chat', name: 'SimpleWebsocket', component: SimpleWebsocket},
    { path: '/chatpage', name: 'StompChatPage', component: StompChatPage}
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
