<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Tab Organizations page</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true">
      <!-- Cartões de Organizações -->
      <div class="organization-cards-container">
        <div class="organization-cards">
          <router-link
            v-for="organization in organizations"
            :key="organization.id"
            :to="`/organization/OrganizationDetail/${organization.id}`"
            class="clickable-card"
            :data-testid="'organization-' + organization.id"
          >
            <ion-card>
              <ion-card-header>
                <ion-card-title>{{ organization.name }}</ion-card-title>
                <ion-card-subtitle>
                  {{ organization.city }}, {{ organization.country }}
                </ion-card-subtitle>
              </ion-card-header>
              <ion-card-content>
                <p><strong>Rating:</strong> {{ organization.rating }}</p>
              </ion-card-content>
            </ion-card>
          </router-link>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script lang="ts">
import { ref, onMounted } from "vue";
import { formatDate } from "@/lib/dateFormatter";
import { SendRequest } from "@/lib/request";

interface Organization {
  id: string;
  name: string;
  email: string;
  address: string;
  city: string;
  country: string;
  lastLoginAt: string;
  rating: number;
}

interface Event {
  id: number;
  title: string;
  category: string;
  startDate: string;
  endDate: string;
  creator: { id: string; name: string };
  location: string;
  rating: number;
  isPromoted: boolean;
}

export default {
  setup() {
    const organizations = ref<Organization[]>([]);

    const loadOrganizations = async () => {
      try {
        const eventsResponse = await SendRequest("/api/events", "GET");
        const events: Event[] = await eventsResponse.json();

        // Filtrar eventos que já finalizaram
        const pastEvents = events.filter(
          (event) => new Date(event.endDate) < new Date()
        );

        // Agrupar eventos por criador
        const eventsByCreator = pastEvents.reduce((acc, event) => {
          if (!acc[event.creator.id]) {
            acc[event.creator.id] = [];
          }
          acc[event.creator.id].push(event);
          return acc;
        }, {} as Record<string, Event[]>);

        // Obter todas as organizações
        const uniqueCreators = new Set(events.map((event) => event.creator.id));
        const organizationPromises = Array.from(uniqueCreators).map(
          (creatorId: string) =>
            SendRequest(`/api/users/getuser?id=${creatorId}`, "GET").then(
              async (response) => {
                const organization = await response.json();
                console.log("organization", organization);
                const creatorEvents = eventsByCreator[creatorId] || [];
                const averageRating =
                  creatorEvents.length > 0
                    ? creatorEvents.reduce(
                        (sum, event) => sum + event.rating,
                        0
                      ) / creatorEvents.length
                    : 0;
                organization.rating = averageRating;
                return organization;
              }
            )
        );

        const organizationResponses = await Promise.all(organizationPromises);
        organizations.value = organizationResponses;
      } catch (error) {
        console.error("Erro ao buscar organizações:", error);
        organizations.value = [];
      }
    };

    onMounted(() => {
      loadOrganizations();
    });

    return {
      organizations,
      formatDate,
    };
  },
};
</script>

<style scoped>
.title {
  text-align: center;
  font-size: 2rem;
  font-weight: bold;
  color: black;
  margin-bottom: 40px;
  animation: fadeIn 1s ease-out;
}

.organization-cards-container {
  max-height: 80vh;
  overflow-y: auto;
  padding: 16px;
}

.organization-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
}

ion-card {
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  height: 100%;
}

ion-card-header {
  background-color: var(--ion-color-light);
  padding: 10px;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

ion-card-content {
  padding: 12px;
  font-size: 14px;
  color: black;
}

ion-card-title {
  text-transform: none;
  font-size: 20px;
  font-weight: bold;
  color: var(--ion-color-dark);
}

ion-card-subtitle {
  font-size: 14px;
  color: var(--ion-color-dark);
}

.clickable-card {
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
  height: 100%;
}
</style>
