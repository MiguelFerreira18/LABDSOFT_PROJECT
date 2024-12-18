import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'io.ionic.starter',
  appName: 'public',
  webDir: 'dist',
  plugins: {
    SplashScreen: {
      launchShowDuration: 0,
    },
    PushNotifications: {
      presentationOptions: ['badge', 'alert', 'sound'],
    },
  },
  android: {
    allowMixedContent: true,
  },
  server: {
    allowNavigation: ['*'],
    cleartext: true,
  },
};

export default config;
