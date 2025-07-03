import { createRouter, createWebHistory } from "vue-router";

const routes = createRouter({
    history: createWebHistory(),
    routes:[
        {
            name:'login',
            path:'/',
            component: () => import('../login.vue')
        }
    ]
})

export default routes;