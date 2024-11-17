import axios from 'axios';

const API_BASE_URL = 'http://localhost:9091/api/events';

// Event model interface
export interface Event {
    id: string;
    title: string;
    startDate: string; // ISO string
    endDate: string; // ISO string
    creator: {
        name: string;
    };
    location: string;
    categories: string[];
}

// Fetch all events
export const fetchAllEvents = async (): Promise<Event[]> => {
    try {
        const response = await axios.get<Event[]>(API_BASE_URL); // Strongly typed response
        return response.data;
    } catch (error) {
        console.error('Error fetching events:', error);
        throw error;
    }
};
