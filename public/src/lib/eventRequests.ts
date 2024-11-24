import axios from "axios";
import { apiConfig, apiKey } from "./config";

const { baseUrl } = apiConfig;

export async function fetchAllEvents(token: string = ""): Promise<any[]> {
  try {
    const response = await fetch(`${baseUrl}/api/events`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "X-API-KEY": apiKey,
      },
    });

    if (!response.ok) {
      throw new Error("Error fetching events");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching events:", error);
    return [];
  }
}
export async function fetchNonPromotedEvents(
  token: string = ""
): Promise<any[]> {
  try {
    const response = await fetch(`${baseUrl}/api/events/non-promoted`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "X-API-KEY": apiKey,
      },
    });

    if (!response.ok) {
      throw new Error("Error fetching events");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching events:", error);
    return [];
  }
}

export async function fetchPromotedEvents(token: string = ""): Promise<any[]> {
  try {
    const response = await fetch(`${baseUrl}/api/events/promoted`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "X-API-KEY": apiKey,
      },
    });

    if (!response.ok) {
      throw new Error("Error fetching events");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching events:", error);
    return [];
  }
}



export const createEvent = async (eventData: {
  title: string;
  location: string;
  startDate: string;
  endDate: string;
  description: string;
  category: string;
  creatorID: string;
  latitude?: number | null;
  longitude?: number | null;
}) => {
  try {
    // Make API request to create event
    const response = await axios.post(`${baseUrl}/api/events`, eventData, {
      headers: {
        "Content-Type": "application/json",
      },
    });

    return response.data; // Return the created event data
  } catch (error: any) {
    if (axios.isAxiosError(error)) {
      const errorMessage = error.response?.data?.error || error.message || "Unknown error occurred.";
      console.error("Error creating event:", errorMessage);
      throw new Error(errorMessage); // Re-throw error for handling in the Vue component
    } else {
      console.error("Unexpected error:", error);
      throw new Error("An unexpected error occurred while creating the event.");
    }
  }
};

export interface EventSummary {
  id: string;
  title: string;
  date: string;
  location: string;
  category: string;
  totalAttendees: number;
};

export const fetchDashboardSummaries = async (userId: string): Promise<EventSummary[]> => {
  try {
    // Adjust the API URL if necessary to match your backend
    const response = await fetch(`${baseUrl}/api/events/dashboard`);
    if (!response.ok) {
      throw new Error('Failed to fetch event summaries');
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching event summaries:', error);
    throw error; // Rethrow for further handling in the component
  }
};

export const fetchCreatedEvents = async (userId: string) => {
  const response = await axios.get(`${baseUrl}/api/events/createdBy/${userId}`);
  return response.data;
};
