<template>
  <div>
    <h1 class="title">Events</h1>

    <div class="event-cards-container">
      <div class="event-cards">
        <router-link :to="`/event/EventDetail/${event.id}`" v-for="event in events" :key="event.id"
          class="clickable-card">
          <ion-card :color="getCardColor(event)">
            <ion-card-header>
              <ion-card-title>{{ event.title }}</ion-card-title>
              <ion-card-subtitle>{{ formatDate(event.startDate) }} - {{ formatDate(event.endDate) }}</ion-card-subtitle>
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
  </div>
</template>

<script>
// Importando a função formatDate do arquivo de utilidades
import { formatDate } from '@/lib/dateFormatter';
import { fetchAllEvents } from '@/lib/eventRequests';
import { IonCard, IonCardContent, IonCardHeader, IonCardSubtitle, IonCardTitle } from '@ionic/vue';
import { defineComponent } from 'vue';

export default defineComponent({
  components: { IonCard, IonCardContent, IonCardHeader, IonCardSubtitle, IonCardTitle },
  data() {
    return {
      events: [],  // Armazena os eventos recebidos
    };
  },
  created() {
    this.loadEvents();  // Carrega eventos ao criar o componente
  },
  methods: {
    async loadEvents() {
      const token = "token";
      const events = await fetchAllEvents(token);  // Chama a função para buscar eventos
      this.events = events;  // Armazena os eventos no estado
    },
    getCardColor(event) {
      // Por enquanto, retorna uma cor fixa para todos os eventos
      return 'light';  // Todos os eventos terão a mesma cor por enquanto

      // Lógica futura que pode ser ativada posteriormente
      // const categories = event.categories.map(category => category.toLowerCase());
      // if (categories.includes('concert')) {
      //   return 'primary';
      // } else if (categories.includes('workshop')) {
      //   return 'secondary';
      // } else if (categories.includes('conference')) {
      //   return 'tertiary';
      // } else {
      //   return 'light';  // Cor padrão
      // }
    }
    ,
    formatDate // A função formatDate agora é parte dos métodos do componente
  }
});
</script>

<style scoped>
.title {
  text-align: center;
  font-size: 2rem;
  font-weight: bold;
  color: var(--ion-color-primary);
  margin-bottom: 20px;
  color: aliceblue;

  /* Efeito de animação de fade-in */
  animation: fadeIn 1s ease-out;
}

/* Animação para o efeito de fade-in */
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