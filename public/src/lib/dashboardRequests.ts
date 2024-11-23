import axios from "axios";
import { apiConfig } from "./config";

const { baseUrl } = apiConfig;
const API_URL = `${baseUrl}/api/events/dashboard`;

export async function fetchDashboardSummaries() {
  const response = await axios.get(API_URL);
  return response.data; // Assumes backend returns a JSON array of EventSummary objects
}
