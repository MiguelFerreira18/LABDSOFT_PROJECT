const SECRET_KEY: string = "b270d27936999e591589f72d98e959b1";
const IV = new Uint8Array(16);
async function getKey(): Promise<CryptoKey> {
  return crypto.subtle.importKey(
    "raw",
    new TextEncoder().encode(SECRET_KEY),
    { name: "AES-CBC" },
    false,
    ["encrypt", "decrypt"]
  );
}

export async function Encrypt(data: string): Promise<string> {
  const key = await getKey();
  const encrypted = await crypto.subtle.encrypt(
    {
      name: "AES-CBC",
      iv: IV,
    },
    key,
    new TextEncoder().encode(data)
  );
  return btoa(String.fromCharCode(...new Uint8Array(encrypted)));
}

export async function Decrypt(encryptedData: string): Promise<string> {
  const key = await getKey();
  const encryptedBytes = Uint8Array.from(atob(encryptedData), (c) =>
    c.charCodeAt(0)
  );
  const decrypted = await crypto.subtle.decrypt(
    {
      name: "AES-CBC",
      iv: IV,
    },
    key,
    encryptedBytes
  );

  return new TextDecoder().decode(decrypted);
}
