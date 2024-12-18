import { apiConfig, apiKey } from './config';
import { IsJWTExpired } from './jwt';

const { baseUrl } = apiConfig;

export async function fetchAllEvents(): Promise<any[]> {
  const token = localStorage.getItem('token') || '';
  try {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      'X-API-KEY': apiKey,
      'ngrok-skip-browser-warning': '69420',
      credentials: 'include',
    };
    const response = await fetch(`${baseUrl}/api/events`, {
      method: 'GET',
      headers: headers,
    });

    if (!response.ok) {
      throw new Error('Error fetching events');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching events:', error);
    return [];
  }
}
export async function fetchNonPromotedEvents(): Promise<any[]> {
  const token = localStorage.getItem('token') || '';
  try {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      'X-API-KEY': apiKey,
      'ngrok-skip-browser-warning': '69420',
      credentials: 'include',
    };
    if (token !== '' && !IsJWTExpired(token)) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    const response = await fetch(`${baseUrl}/api/events/non-promoted`, {
      method: 'GET',
      headers: headers,
    });

    if (!response.ok) {
      throw new Error('Error fetching events');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching events:', error);
    return [];
  }
}

export async function fetchPromotedEvents(): Promise<any[]> {
  const token = localStorage.getItem('token') || '';
  try {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      'X-API-KEY': apiKey,
      'ngrok-skip-browser-warning': '69420',
      credentials: 'include',
    };
    if (token !== '' && !IsJWTExpired(token)) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    const response = await fetch(`${baseUrl}/api/events/promoted`, {
      method: 'GET',
      headers: headers,
    });

    if (!response.ok) {
      throw new Error('Error fetching events');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching events:', error);
    return [];
  }
}
