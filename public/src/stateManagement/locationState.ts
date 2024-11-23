import { reactive } from 'vue';

export const locationState = reactive({
  onLocationSelected: null as null | ((location: { latitude: number; longitude: number }) => void),
});