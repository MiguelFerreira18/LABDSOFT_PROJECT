import axios from 'axios';

const API_BASE_URL = 'http://localhost:9091/api/events';

// Event model interface

export interface Event {
    id?: string;
    title: string;
    location: string;
    startDate: string; // ISO date string
    endDate: string; // ISO date string
    description: string;
    categories: string[];
    imagePath?: string;
    creator?: {
      id: string;
      name: string;
      email: string;
    };
    attendees?: Array<{
      id: string;
      name: string;
      email: string;
    }>;
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

/**
 * Creates a new event.
 */import axios from 'axios';

// Base API URL
const BASE_URL = 'http://localhost:9091/api/events';

/**
 * Creates a new event by sending a POST request to the API.
 * 
 * @param eventData - The event data to be created.
 * @returns The created event from the API response.
 */
export const createEvent = async (eventData: {
  title: string;
  location: string;
  startDate: string;
  endDate: string;
  description: string;
  categories: string; // Array of category strings
}) => {
  try {
    const response = await axios.post(BASE_URL, eventData, {
      headers: {
        'Content-Type': 'application/json', // Ensures the request is sent as JSON
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error creating event:', error);
    throw error;
  }
};


  /**
   * Updates an existing event by ID.
   */
  export const updateEvent = async (id: string, event: Event): Promise<Event> => {
    const response = await axios.put<Event>(`${API_BASE_URL}/${id}`, event);
    return response.data;
  };
