<template>
  <ion-content class="ion-padding">
    <h1 class="title">Events</h1>

    <div class="event-cards-container">
      <div class="event-cards">
        <router-link :to="`/event/EventDetail/${event.id}`" v-for="event in events" :key="event.id"
          class="clickable-card">
          <ion-card :color="getCardColor(event)">
            <ion-card-header>
              <ion-card-title>{{ event.title }}</ion-card-title>
              <ion-card-subtitle>
                {{ formatDate(event.startDate) }} - {{ formatDate(event.endDate) }}
              </ion-card-subtitle>
            </ion-card-header>

            <ion-card-content>
              <p><strong>Creator:</strong> {{ event.creator.name }}</p>
              <p><strong>Location:</strong> {{ event.location }}</p>
              <p><strong>Categories:</strong> {{ event.categories.join(', ') }}</p>
            </ion-card-content>
          </ion-card>
        </router-link>
      </div>
    </div>
  </ion-content>
</template>

<script>
import { ref, onMounted } from 'vue';
import { formatDate } from '@/lib/dateFormatter'; // Supondo que formatDate esteja em dateFormatter.js
import { fetchAllEvents } from '@/lib/eventRequests';
import {
  IonCard,
  IonCardContent,
  IonCardHeader,
  IonCardSubtitle,
  IonCardTitle,
} from '@ionic/vue';

export default {
  components: {
    IonCard,
    IonCardContent,
    IonCardHeader,
    IonCardSubtitle,
    IonCardTitle,
  },
  setup() {
    const events = ref([]);

    // Função para carregar os eventos
    const loadEvents = async () => {
      try {
        // Chama a função fetchAllEvents para pegar os dados
        const data = await fetchAllEvents();
        events.value = data; // Atribui os dados obtidos à variável events
      } catch (error) {
        console.error('Error fetching events:', error);
      }
    };

    // Função para definir a cor do cartão com base nas categorias
    const getCardColor = (event) => {
      const categories = event.categories.map((category) => category.toLowerCase());
      /*if (categories.includes('concert')) {
        return 'primary';
      } else if (categories.includes('workshop')) {
        return 'secondary';
      } else if (categories.includes('conference')) {
        return 'tertiary';
      } else {}*/
      return 'light'; // Cor padrão

    };

// Chama a função loadEvents quando o componente é montado
      onMounted(loadEvents);
  
      return {
        events,
        getCardColor,
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
}

ion-card-title {
  text-transform: none;
  font-size: 20px;
  font-weight: bold;
}

ion-card-subtitle {
    font-size: 14px;
    color: var(--ion-color-medium);
  }
  
  .clickable-card {
    text-decoration: none;
    color: inherit;
  }
  
</style>
  
