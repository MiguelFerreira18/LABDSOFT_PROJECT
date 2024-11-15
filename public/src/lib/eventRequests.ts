import { apiConfig, apiKey } from "./config";  

const { baseUrl } = apiConfig;

export async function fetchAllEvents(token: string = ""): Promise<any[]> {
    try {
      const response = await fetch(`${baseUrl}/api/events`, {
        method: "GET",  
        headers: {
          "Content-Type": "application/json", 
          "X-API-KEY": apiKey,  
          // Remova o cabeçalho de Authorization completamente, já que o backend permite sem autenticação
        },
      });
  
      if (!response.ok) {
        throw new Error("Erro ao buscar eventos");
      }
  
      return await response.json(); 
    } catch (error) {
      console.error("Erro ao buscar eventos:", error);
      return []; 
    }
  }
  