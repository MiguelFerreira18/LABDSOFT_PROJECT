<template>
  <ion-content class="ion-padding">
    <h1 class="title">Events</h1>

    <!-- Botão para abrir/fechar o filtro -->
    <div class="button-container">
      <button class="filter-button" @click="toggleDropdown" data-testid="toggle-dropdown">
        <svg viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
          <g fill="none" stroke="white" stroke-linecap="round" stroke-linejoin="round" stroke-width="32">
            <path d="m368 128h80" />
            <path d="m64 128h240" />
            <path d="m368 384h80" />
            <path d="m64 384h240" />
            <path d="m208 256h240" />
            <path d="m64 256h80" />
            <circle cx="336" cy="128" r="32" />
            <circle cx="176" cy="256" r="32" />
            <circle cx="336" cy="384" r="32" />
          </g>
        </svg>
      </button>
    </div>

    <!-- Dropdown com filtros de categoria e data -->
    <div v-if="showDropdown" class="dropdown-menu">
      <label for="category-limit">Filter by Category:</label>
      <ion-button v-for="category in categories" :key="category" @click="toggleCategory(category)"
        :data-testid="'filter-' + category">
        {{ category }}
      </ion-button>

      <!-- Filtro por data -->
      <div class="date-filter">
        <label for="date-limit">Filter by Date:</label>
        <input id="date-limit" type="date" v-model="dateLimit" data-testid="date-filter" />
      </div>

      <ion-button color="danger" @click="clearFilters()" class="clear-button" data-testid="clear-filters">
        Clear
      </ion-button>
    </div>

    <!-- Cartões de Eventos -->
    <div class="event-cards-container">
      <div class="event-cards">
        <router-link v-for="event in filteredEvents" :key="event.id" :to="`/event/EventDetail/${event.id}`"
          class="clickable-card" :data-testid="'event-' + event.id">
          <ion-card>
            <ion-card-header :style="{ backgroundColor: categoryColors[event.category] || '#ccc' }">
              <ion-card-title>{{ event.title }}</ion-card-title>
              <ion-card-subtitle>
                {{ formatDate(event.startDate) }} - {{ formatDate(event.endDate) }}
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
</template>

<script lang="ts">
import { ref, computed, onMounted } from 'vue';
import { fetchAllEvents } from '@/lib/eventRequests';
import { formatDate } from '@/lib/dateFormatter';
import { categories, categoryColors } from '@/lib/categories'

interface Event {
  id: number;
  title: string;
  category: string;
  startDate: string;
  endDate: string;
  creator: { name: string };
  location: string;
}

export default {
  setup() {
    const events = ref<Event[]>([]); // Lista de eventos
    const selectedCategories = ref<string[]>([]); // Categorias selecionadas pelo usuário
    const dateLimit = ref<string | null>(null); // Limite de data escolhido
    const showDropdown = ref(false); // Controle de visibilidade do dropdown

    // Função para carregar eventos da API
    const loadEvents = async () => {
      try {
        //@ts-expect-error - Erro estupido do typescript 
        events.value = await fetchAllEvents();
      } catch (error) {
        console.error('Erro ao buscar eventos:', error);
        events.value = []; // Caso de erro, deixa a lista vazia
      }
    };

    // Computed para filtrar eventos com base nas categorias e data
    const filteredEvents = computed(() => {
      return events.value.filter((event) => {
        const isCategoryMatch =
          selectedCategories.value.length === 0 ||
          selectedCategories.value.includes(event.category);

        const isDateMatch =
          !dateLimit.value || new Date(event.endDate) <= new Date(dateLimit.value);

        return isCategoryMatch && isDateMatch;
      });
    });

    // Alterna a exibição do dropdown
    const toggleDropdown = () => {
      showDropdown.value = !showDropdown.value;
    };

    // Alterna a seleção de uma categoria
    const toggleCategory = (category: string) => {
      const index = selectedCategories.value.indexOf(category);
      if (index >= 0) {
        selectedCategories.value.splice(index, 1);
      } else {
        selectedCategories.value.push(category);
      }
    };

    // Limpa todos os filtros aplicados
    const clearFilters = () => {
      selectedCategories.value = [];
      dateLimit.value = null;
    };

    // Chama a função de carregar eventos ao montar o componente
    onMounted(loadEvents);

    return {
      events,
      categories,
      selectedCategories,
      dateLimit,
      filteredEvents,
      toggleCategory,
      toggleDropdown,
      showDropdown,
      formatDate,
      clearFilters,
      categoryColors,
    };
  },
};
</script>



<style scoped>
.title {
  text-align: center;
  font-size: 2rem;
  font-weight: bold;
  color: aliceblue;
  margin-bottom: 20px;
  animation: fadeIn 1s ease-out;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
    transform: translateY(-20px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
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
  color: aliceblue;
}

ion-card-title {
  text-transform: none;
  font-size: 20px;
  font-weight: bold;
  color: aliceblue;

}

ion-card-subtitle {
  font-size: 14px;
  color: aliceblue;
}

.clickable-card {
  text-decoration: none;
  color: inherit;
}

.dropdown-menu {
  position: absolute;
  margin-top: 20px;
  right: 16px;
  background: var(--ion-color-light);
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 10;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.date-filter {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 10px;
}

.date-filter label {
  font-weight: bold;
  color: var(--ion-color-dark);
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
  top: 70px;
  right: 16px;
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  width: 48px;
  height: 48px;
  z-index: 100;
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

.clear-button {
  margin-top: 10px;
  align-self: flex-end;
  /* Alinha o botão à direita */
  font-size: 14px;
}
</style>
