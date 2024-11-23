import axios from "axios";
import { apiConfig } from "./config";

const { baseUrl } = apiConfig;
const API_BASE_URL = `${baseUrl}/api/events`;

// Event model interface

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
    export async function fetchNonPromotedEvents(token: string = ""): Promise<any[]> {
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
  