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
    buildOptions: {
      releaseType: 'APK',
      keystoreAlias: 'my-key-alias',
      keystorePassword: 'password',
      keystorePath: './my-release-key.jks',
      keystoreAliasPassword: 'password',
    },
  },
  server: {
    allowNavigation: ['*'],
    cleartext: true,
  },
};

export default config;
