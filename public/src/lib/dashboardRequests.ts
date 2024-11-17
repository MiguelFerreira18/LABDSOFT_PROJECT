import axios from 'axios';

const API_URL = 'http://localhost:9091/api/events/dashboard';

export async function fetchDashboardSummaries() {
    const response = await axios.get(API_URL);
    return response.data; // Assumes backend returns a JSON array of EventSummary objects
}
