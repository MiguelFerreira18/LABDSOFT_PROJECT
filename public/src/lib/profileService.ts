import { apiConfig, apiKey } from './config';
import { IsJWTExpired } from './jwt';

const { baseUrl } = apiConfig;

// Utility to get user ID from localStorage
function getUserIdFromLocalStorage(): string {
  return localStorage.getItem('userId') || ''; // Return empty string if no userId found
}

// Fetch User Information
export async function fetchUserInfo(userId: string): Promise<any | null> {
  const token = localStorage.getItem('token') || '';
  try {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      'X-API-KEY': apiKey,
      'ngrok-skip-browser-warning': '69420',
      credentials: 'include',
    };

    if (token && !IsJWTExpired(token)) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${baseUrl}/api/users/getuser?id=${userId}`, {
      method: 'GET',
      headers: headers,
    });

    if (!response.ok) {
      throw new Error('Error fetching user info');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching user info:', error);
    return null;
  }
}

// Fetch User Points
export async function fetchUserPoints(userId: string): Promise<number> {
  const token = localStorage.getItem('token') || '';
  try {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      'X-API-KEY': apiKey,
      'ngrok-skip-browser-warning': '69420',
      credentials: 'include',
    };

    if (token && !IsJWTExpired(token)) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${baseUrl}/api/rewards/points/${userId}`, {
      method: 'GET',
      headers: headers,
    });

    if (!response.ok) {
      throw new Error('Error fetching user points');
    }

    const { data } = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching user points:', error);
    return 0;
  }
}

// Define the response type for the badges
interface Badge {
  id: string;
  name: string;
  iconPath: string;
  milestone?: {
    name: string;
    description: string;
  };
}

interface BadgesResponse {
  badges: Badge[];
}

// Fetch User Badges with additional checks
export const fetchUserBadges = async (userId: string): Promise<Badge[]> => {
    try {
      // Send a POST request to fetch badges based on the userId
      const response = await fetch(`${baseUrl}/api/profile/badges`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userId }), // Send the userId in the request body
      });
  
      // Check if the response is okay
      if (!response.ok) {
        throw new Error('Error fetching user badges');
      }
  
      // Parse the response JSON
      const data = await response.json();
      console.log('Fetched badges data:', data); // Log the data for debugging
  
      // Return the badges array if available, otherwise return an empty array
      return Array.isArray(data.badges) ? data.badges : [];
    } catch (error) {
      console.error('Error fetching user badges:', error);
      return [];
    }
  };

// Fetch All Badges (Admin/General Use)
export async function fetchAllBadges(): Promise<Badge[]> {
  try {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      'X-API-KEY': apiKey,
      'ngrok-skip-browser-warning': '69420',
      credentials: 'include',
    };

    const response = await fetch(`${baseUrl}/api/profile/badges/all`, {
      method: 'GET',
      headers: headers,
    });

    if (!response.ok) {
      throw new Error('Error fetching all badges');
    }

    const { badges } = await response.json();
    console.log(badges)
    return badges || [];
  } catch (error) {
    console.error('Error fetching all badges:', error);
    return [];
  }
}