<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title class="text-h5">
            회원 목록
          </v-card-title>

          <v-card-text>
            <v-alert v-if="errorMessage" type="error" density="compact" class="mb-3">
              {{ errorMessage }}
            </v-alert>

            <v-table v-if="members.length > 0">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>이름</th>
                  <th>Email</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="member in members" :key="member.id">
                  <td>{{ member.id }}</td>
                  <td>{{ member.name }}</td>
                  <td>{{ member.email }}</td>
                </tr>
              </tbody>
            </v-table>

            <v-alert v-if="!loading && members.length === 0 && !errorMessage" type="info">
              등록된 회원이 없습니다.
            </v-alert>

            <div v-if="loading" class="text-center pa-4">
              <v-progress-circular indeterminate color="primary" />
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  name: "MemberList",
  data() {
    return {
      members: [],
      errorMessage: "",
      loading: false
    };
  },
  created() {
    this.fetchMemberList();
  },
  methods: {
    async fetchMemberList() {
      const token = localStorage.getItem("token");
      if (!token) {
        this.$router.push("/login");
        return;
      }

      this.loading = true;
      this.errorMessage = "";

      try {
        const res = await axios.get("http://localhost:8080/member/list", {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        this.members = res.data || [];
      } catch (error) {
        if (error.response?.status === 401) {
          localStorage.removeItem("token");
          localStorage.removeItem("memberId");
          this.$router.push("/login");
        } else {
          this.errorMessage = "회원 목록을 불러오는데 실패했습니다.";
        }
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>
