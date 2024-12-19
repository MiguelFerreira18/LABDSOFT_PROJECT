<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Events</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content class="ion-padding">
      <ion-fab horizontal="end">
        <ion-fab-button @click="toggleDropdown">
          <ion-icon :icon="filterCircleOutline"></ion-icon>
        </ion-fab-button>
      </ion-fab>

      <!-- Barra Horizontal de Eventos Promovidos -->
      <div v-if="filteredPromotedEvents.length" class="promoted-events-section">
        <h2 class="subtitle">Promoted Events</h2>
        <div class="promoted-events-bar">
          <router-link
            v-for="event in filteredPromotedEvents"
            :key="event.id"
            :to="`/event/EventDetail/${event.id}`"
            class="clickable-card"
            :data-testid="'promoted-event-' + event.id"
          >
            <ion-card>
              <ion-card-header
                :style="{
                  backgroundColor: categoryColors[event.category] || '#ccc',
                }"
              >
                <ion-card-title>{{ event.title }}</ion-card-title>
                <ion-card-subtitle>
                  {{ formatDate(event.startDate) }} -
                  {{ formatDate(event.endDate) }}
                </ion-card-subtitle>
              </ion-card-header>
              <ion-card-content>
                <p><strong>Creator:</strong> {{ event.creator.name }}</p>
                <p><strong>Location:</strong> {{ event.location }}</p>
                <p><strong>Category:</strong> {{ event.category }}</p>
              </ion-card-content>
            </ion-card>
          </router-link>
        </div>
      </div>

      <!-- Dropdown com filtros de categoria e data -->
      <div v-if="showDropdown" class="dropdown-menu">
        <label for="category-limit">Filter by Category:</label>
        <ion-button
          v-for="category in categories"
          :key="category"
          @click="toggleCategory(category)"
          :data-testid="'filter-' + category"
        >
          {{ category }}
        </ion-button>

        <!-- Filtro por data -->
        <div class="date-filter">
          <label for="date-limit">Filter by Date:</label>
          <input
            style="background-color: var(--ion-color-light)"
            id="date-limit"
            type="date"
            v-model="dateLimit"
            data-testid="date-filter"
          />
        </div>

        <ion-button
          color="danger"
          @click="clearFilters()"
          class="clear-button"
          data-testid="clear-filters"
        >
          Clear
        </ion-button>
      </div>

      <!-- Cartões de Eventos -->
      <div class="event-cards-container">
        <div class="event-cards">
          <router-link
            v-for="event in filteredNonPromotedEvents"
            :key="event.id"
            :to="`/event/EventDetail/${event.id}`"
            class="clickable-card"
            :data-testid="'event-' + event.id"
          >
            <ion-card>
              <ion-card-header
                :style="{
                  backgroundColor: categoryColors[event.category] || '#ccc',
                }"
              >
                <ion-card-title>{{ event.title }}</ion-card-title>
                <ion-card-subtitle>
                  {{ formatDate(event.startDate) }} -
                  {{ formatDate(event.endDate) }}
                </ion-card-subtitle>
              </ion-card-header>
              <ion-card-content>
                <p><strong>Creator:</strong> {{ event.creator.name }}</p>
                <p><strong>Location:</strong> {{ event.location }}</p>
                <p><strong>Category:</strong> {{ event.category }}</p>
              </ion-card-content>
            </ion-card>
          </router-link>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script lang="ts">
import { ref, computed, onMounted } from 'vue';
import {
  fetchNonPromotedEvents,
  fetchPromotedEvents,
} from '@/lib/eventRequests';
import { formatDate } from '@/lib/dateFormatter';
import { categories, categoryColors } from '@/lib/categories';
import { GoogleGenerativeAI } from '@google/generative-ai';
import { SendRequest } from '@/lib/request';
import { filterCircleOutline } from 'ionicons/icons';

interface Event {
  id: number;
  title: string;
  category: string;
  startDate: string;
  endDate: string;
  creator: { name: string };
  location: string;
  isPromoted: boolean;
}

const genAI = new GoogleGenerativeAI('AIzaSyCAIAOQ-T9cc3OdW-LYXJyaEnwANenGQA4');
const model = genAI.getGenerativeModel({ model: 'gemini-1.5-flash' });

async function generateRecommendations(events: Event[]) {
  const userId = localStorage.getItem('uuid') || '';
  const endpoint = '/subscription/event/';
  const response = await SendRequest(`${endpoint}${userId}`, 'GET');
  const subscribedEvents = await response.json();

  try {
    const prompt = `Based on the following events you have attended or are subscribed to: ${JSON.stringify(subscribedEvents)}. Which of the following events do you recommend attending, excluding the ones you have attended or are subscribed to? ${JSON.stringify(events)}. Please respond with an array containing only the IDs of the recommended events in the format: [1, 2, 3].`;

    const result = await model.generateContent(prompt);
    const responseText = await result.response.text();

    const sanitizedResponse = responseText
      .replace(/```json/g, '')
      .replace(/```/g, '')
      .trim();

    return JSON.parse(sanitizedResponse) || [];
  } catch (error) {
    console.error('Erro ao chamar a API:', error);
    return [];
  }
}

export default {
  setup() {
    const promotedEvents = ref<Event[]>([]);
    const nonPromotedEvents = ref<Event[]>([]);
    const selectedCategories = ref<string[]>([]);
    const dateLimit = ref<string | null>(null);
    const showDropdown = ref(false);
    const recommendedEventIds = ref<number[]>([]);

    const loadPromoted = async () => {
      try {
        promotedEvents.value = await fetchPromotedEvents();
      } catch (error) {
        promotedEvents.value = [];
      }
    };

    const loadNonPromoted = async () => {
      try {
        nonPromotedEvents.value = await fetchNonPromotedEvents();
      } catch (error) {
        nonPromotedEvents.value = [];
      }
    };

    const filteredNonPromotedEvents = computed(() => {
      return nonPromotedEvents.value.filter(
        (event) =>
          (!recommendedEventIds.value.length ||
            recommendedEventIds.value.includes(event.id)) &&
          (selectedCategories.value.length === 0 ||
            selectedCategories.value.includes(event.category)) &&
          (!dateLimit.value ||
            new Date(event.endDate) <= new Date(dateLimit.value))
      );
    });

    const filteredPromotedEvents = computed(() => {
      return promotedEvents.value.filter(
        (event) =>
          (!recommendedEventIds.value.length ||
            recommendedEventIds.value.includes(event.id)) &&
          (selectedCategories.value.length === 0 ||
            selectedCategories.value.includes(event.category)) &&
          (!dateLimit.value ||
            new Date(event.endDate) <= new Date(dateLimit.value))
      );
    });

    const toggleCategory = async (category: string) => {
      if (category === 'Artificial Intelligence') {
        recommendedEventIds.value = await generateRecommendations([
          ...promotedEvents.value,
          ...nonPromotedEvents.value,
        ]);
      } else {
        const index = selectedCategories.value.indexOf(category);
        if (index >= 0) {
          selectedCategories.value.splice(index, 1);
        } else {
          selectedCategories.value.push(category);
        }
      }
    };

    const toggleDropdown = () => {
      showDropdown.value = !showDropdown.value;
    };

    const clearFilters = () => {
      selectedCategories.value = [];
      dateLimit.value = null;
      recommendedEventIds.value = [];
    };

    onMounted(() => {
      loadPromoted();
      loadNonPromoted();
    });

    return {
      promotedEvents,
      nonPromotedEvents,
      categories,
      selectedCategories,
      dateLimit,
      filteredNonPromotedEvents,
      filteredPromotedEvents,
      toggleCategory,
      toggleDropdown,
      showDropdown,
      formatDate,
      clearFilters,
      categoryColors,
      filterCircleOutline,
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
  /* Aumenta o espaçamento inferior entre o título e o conteúdo */
  animation: fadeIn 1s ease-out;
}

.promoted-events-section {
  margin-bottom: 40px;
  /* Aumenta o espaçamento inferior entre a seção de eventos promovidos e o conteúdo abaixo */
}

.promoted-events-bar {
  display: flex;
  overflow-x: auto;
  gap: 16px;
  margin-top: 10px;
}

.promoted-events-bar .clickable-card {
  width: 250px;
  height: 100%;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.event-cards-container {
  max-height: 80vh;
  overflow-y: auto;
  padding: 16px;
}

.event-cards {
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
  color: black;
}

ion-card-subtitle {
  font-size: 14px;
  color: black;
}

.clickable-card {
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.dropdown-menu {
  position: fixed;
  top: 130px;
  /* Ajusta para ficar logo abaixo do botão */
  right: 16px;
  background: var(--ion-color-light);
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 200;
  /* Garante que o dropdown sobreponha outros elementos */
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 250px;
  /* Ajuste a largura para que não fique tão grande */
}

.date-filter {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 10px;
}

.date-filter label {
  font-weight: bold;
  color: blue;
}

.dropdown-menu label {
  font-weight: bold;
  color: var(--ion-color-dark);
}

.date-filter input {
  padding: 8px;
  border: 1px solid var(--ion-color-medium);
  border-radius: 4px;
  font-size: 16px;
}

.filter-button {
  position: fixed;
  top: 80px;
  right: 20px;
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  width: 48px;
  height: 48px;
  z-index: 100;
  /* Garante que o botão sempre fique visível */
}

.filter-button svg {
  display: block;
  width: 100%;
  height: 100%;
  stroke: white;
}

.filter-button:focus {
  outline: none;
}

.filter-button:hover {
  opacity: 0.8;
}

.promoted-events-section,
.event-cards-container {
  z-index: 1;
  /* Garante que os eventos fiquem abaixo do dropdown */
}

.promoted-events-section h2 {
  margin-top: 60px;
  /* Aumenta o espaçamento entre o ícone do filtro e o título */
  margin-bottom: 16px;
  /* Espaçamento inferior do título */
  font-size: 1.5rem;
  font-weight: bold;
  color: black;
}

.clear-button {
  margin-top: 10px;
  align-self: flex-end;
  font-size: 14px;
}

.promoted-events-section,
.event-cards-container {
  z-index: 1;
  /* Garante que os eventos fiquem abaixo do dropdown */
}

.clear-button {
  margin-top: 10px;
  align-self: flex-end;
  font-size: 14px;
}
</style>
