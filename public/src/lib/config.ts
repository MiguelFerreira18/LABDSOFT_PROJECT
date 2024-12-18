const isProduction = import.meta.env.VITE_NODE_ENV === 'production';
const apiKey = import.meta.env.VITE_API_KEY; //NOTE: This might not be needed but if further security is needed it can be used

const devApiConfig: ServerStruct = {
  baseUrl: 'http://localhost:9091', //NOTE: Switch this to http://tarpon-allowed-moray.ngrok-free.app if you want to use the mobie app :D
};

const prodApiConfig: ServerStruct = {
  baseUrl: 'https://tarpon-allowed-moray.ngrok-free.app',
};

interface ServerStruct {
  baseUrl: string;
}

console.log('isProduction', isProduction);

const apiConfig = isProduction ? prodApiConfig : devApiConfig;
export { apiConfig, apiKey };
