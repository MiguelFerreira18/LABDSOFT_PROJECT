export interface Event {
  id: string;
  title: string;
  category: string;
  startDate: string;
  endDate: string;
  creator: { name: string };
  location: string;
  isPromoted: boolean;
  latitude: number;
  longitude: number;
}
