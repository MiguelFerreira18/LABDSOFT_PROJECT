import { PushNotifications } from '@capacitor/push-notifications';

export const initializePushNotifications = async () => {
  // Request permissions
  const permission = await PushNotifications.requestPermissions();
  if (permission.receive === 'granted') {
    // Register for push notifications
    await PushNotifications.register();
  }

  // Handle registration success
  PushNotifications.addListener('registration', (token) => {
    console.log('Push registration token:', token.value);
    localStorage.setItem('pushToken', token.value);
    // Send this token to your backend to register the device
  });

  // Handle registration error
  PushNotifications.addListener('registrationError', (error) => {
    console.error('Push registration error:', error);
  });

  // Listen to incoming push notifications
  PushNotifications.addListener('pushNotificationReceived', (notification) => {
    console.log('Push received:', notification);
    // Handle notification data
  });

  // Handle push notification taps
  PushNotifications.addListener('pushNotificationActionPerformed', (action) => {
    console.log('Notification action performed:', action);
    // Handle the user interaction
  });
};
