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
        throw new Error("Error fetching evetns");
      }
  
      return await response.json(); 
    } catch (error) {
      console.error("Error fetching events:", error);
      return []; 
    }
  }
  