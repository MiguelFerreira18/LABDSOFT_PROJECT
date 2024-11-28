<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Tab 2</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true">
      <canvas ref="canvas"></canvas>
      <button @click="scanBarcode">Scan QR Code</button>
      <div v-if="scanResult">
        <p>Scan Result:</p>
        <p>{{ scanResult }}</p>
      </div>
      <ion-header collapse="condense">
        <ion-toolbar>
          <ion-title size="large">Tab 2</ion-title>
        </ion-toolbar>
      </ion-header>

      <ExploreContainer name="Tab 2 page" />
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { IonPage, IonHeader, IonToolbar, IonTitle, IonContent } from '@ionic/vue';
import { CapacitorBarcodeScanner } from '@capacitor/barcode-scanner';
import { Capacitor } from '@capacitor/core';
import ExploreContainer from '@/components/ExploreContainer.vue';
import QRCode from 'qrcode';
import { ref } from 'vue';
import { onMounted } from 'vue';

const scanResult = ref<string | null>(null);
const canvas = ref<HTMLCanvasElement | null>(null);
const text = ref<string>('Hello World');

function generateQRCode() {
  if (canvas.value) {
    QRCode.toCanvas(canvas.value, text.value, function (error: any) {
      if (error) console.error(error)
      console.log('success!');
    })
  }
}
async function scanBarcode() {
  try {
    // Check platform
    const isAvailable = Capacitor.isPluginAvailable('Camera');
    if (!isAvailable) {
      alert('Camera plugin not available');
      return;
    }
    const result = await CapacitorBarcodeScanner.scanBarcode({
      hint: 0 || 17,
      cameraDirection: 1,
    });

    if (result && result.ScanResult) {
      scanResult.value = result.ScanResult;
    } else {
      alert('No barcode detected');
    }
  } catch (error) {
    console.error('Barcode scan error:', error);
    alert('Failed to scan barcode');
  }
}
onMounted(() => {
  generateQRCode()
})

</script>
