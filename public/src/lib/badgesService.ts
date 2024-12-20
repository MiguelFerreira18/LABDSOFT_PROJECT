import { apiConfig, apiKey } from './config';

import { IsJWTExpired } from './jwt';

const { baseUrl } = apiConfig;

// Define interfaces for the response data
interface Badge {
  id: string;
  name: string;
  iconPath: string;
}

interface Milestone {
  id: string;
  name: string;
  description: string;
  category: string;
}

interface BadgesResponse {
  badges: Badge[];
}

interface MilestonesResponse {
  milestones: Milestone[];
}

// Function to get user ID from localStorage
const getUserIdFromLocalStorage = (): string => {
  const userId = localStorage.getItem('userId');
  if (!userId) {
    throw new Error('User ID not found in localStorage');
  }
  return userId;
};

// Function to get badges for a user
export const getUserBadges = async (): Promise<BadgesResponse> => {
  try {
    const userId = getUserIdFromLocalStorage(); // Get userId from localStorage
    const response = await fetch(`${baseUrl}/api/profile/badges`, { // Fixed the URL concatenation
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ userId }), // Send the userId in the request body
    });

    if (!response.ok) {
      throw new Error('Error fetching user badges');
    }

    const badges = await response.json();
    return { badges };
  } catch (error) {
    console.error('Error fetching user badges:', error);
    throw error;
  }
};

export const getAllMilestones = async (): Promise<MilestonesResponse> => {
  try {
    const response = await fetch(`${baseUrl}/api/profile/milestones/all`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error('Error fetching all milestones');
    }

    const milestones = await response.json();
    return { milestones };
  } catch (error) {
    console.error('Error fetching all milestones:', error);
    throw error;
  }
};

