const isProduction = import.meta.env.VITE_NODE_ENV === "production";
const apiKey = import.meta.env.VITE_API_KEY; //NOTE: This might not be needed but if further security is needed it can be used

const devApiConfig: ServerStruct = {
  baseUrl: "http://localhost:9091",
};

const prodApiConfig: ServerStruct = {
  baseUrl: "http://localhost:9091",
};

interface ServerStruct {
  baseUrl: string;
}

const apiConfig = isProduction ? prodApiConfig : devApiConfig;
export { apiConfig, apiKey };
