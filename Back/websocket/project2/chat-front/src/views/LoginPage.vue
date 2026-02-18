<template>
    <v-container>
        <v-row justify="center">
            <v-col cols="12" sm="4" md="6">
                <v-card>
                    <v-card-title class="text-h5 text-center">
                        로그인
                    </v-card-title>

                    <v-card-text>
                        <v-form @submit.prevent="doLogin">
                            <v-text-field
                                label="Email"
                                v-model="email"
                                type="email"
                                required
                            />

                            <v-text-field
                                label="Password"
                                v-model="password"
                                type="password"
                                required
                            />

                            <v-alert v-if="errorMessage" type="error" density="compact" class="mb-3">
                                {{ errorMessage }}
                            </v-alert>

                            <v-btn type="submit" color="primary" block :loading="loading">
                                로그인
                            </v-btn>
                        </v-form>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

export default {
    name: "LoginPage",
    data() {
        return {
            email: "",
            password: "",
            errorMessage: "",
            loading: false
        };
    },
    methods: {
        async doLogin() {
            this.errorMessage = "";
            this.loading = true;

            try {
                const res = await axios.post("http://localhost:8080/member/doLogin", {
                    email: this.email,
                    password: this.password
                });

                const token = res.data.token;
                const role = jwtDecode(token).role; //토큰을 디코딩 후 role을 가져온다.
                const email = jwtDecode(token).sub;
                

                localStorage.setItem("token", token);
                localStorage.setItem("role", role);
                localStorage.setItem("email", email);
                window.location.href = "/";
            } catch (error) {
                this.errorMessage = error.response?.data?.message || "이메일 또는 비밀번호를 확인해주세요.";
                this.loading = false;
            }
        }
    }
};
</script>
