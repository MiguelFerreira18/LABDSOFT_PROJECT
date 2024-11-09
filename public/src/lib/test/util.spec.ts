import { describe, expect, test } from "vitest";
import { ConfirmPasswordMatch, IsAGoodPassword } from "@/lib/signUpUtil";
import { ParseJwt, IsJWTExpired } from "../jwt";
import { Encrypt, Decrypt } from "../encryption";

describe("confirmPasswordMatch", () => {
  test("passwords match", () => {
    const password = "password";
    const confirmPassword = "password";
    expect(ConfirmPasswordMatch(password, confirmPassword)).toBe(true);
  });
  test("passwords do not match", () => {
    const password = "password";
    const confirmPassword = "notPassword";
    expect(ConfirmPasswordMatch(password, confirmPassword)).toBe(false);
  });
});

describe("isAGoodPassword", () => {
  test("password is good", () => {
    const password = "Password1!";
    expect(IsAGoodPassword(password)).toBe(true);
  });
  test("password is too short", () => {
    const password = "Pass1!";
    expect(IsAGoodPassword(password)).toBe(false);
  });
  test("password is too long", () => {
    const password = "a".repeat(129);
    expect(IsAGoodPassword(password)).toBe(false);
  });
  test("password does not contain a lowercase letter", () => {
    const password = "PASSWORD1!";
    expect(IsAGoodPassword(password)).toBe(false);
  });
  test("password does not contain an uppercase letter", () => {
    const password = "password1!";
    expect(IsAGoodPassword(password)).toBe(false);
  });
  test("password does not contain a number", () => {
    const password = "Password!";
    expect(IsAGoodPassword(password)).toBe(false);
  });
  test("password does not contain a special character", () => {
    const password = "Password1";
    expect(IsAGoodPassword(password)).toBe(false);
  });
});

describe("parseJwt", () => {
  test("parses a JWT", () => {
    const token =
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTczMTE0OTQxNiwiZXhwIjoxNzMxMTUzMDE2fQ.c9gPRmiV31TvnQszuJBbOhp07FADtCR8lwCwqjh04rM";
    const parsed = ParseJwt(token);
    expect(parsed).toEqual({
      sub: "1234567890",
      name: "John Doe",
      admin: true,
      iat: 1731149416,
      exp: 1731153016,
    });
  });

  test("JWT is expired", () => {
    const token =
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTczMTE0OTkxNiwiZXhwIjoxNzMxMTQwMDAwfQ.xdzdT5kn_j2LCwxqAhFTjtrDsmryCbguee6-qkB2Oy4";
    expect(IsJWTExpired(token)).toBe(true);
  });
});

describe("encryption", () => {
  test("encrypts and decrypts a string", async () => {
    const data = "test1";
    const encrypted = "FGo0I6QjtSlH30bgBx0Sqw==";
    const encryptedData = await Encrypt(data);
    expect(encryptedData).toBe(encrypted);
  });

  test("decrypts an encrypted string", async () => {
    const data = "test1";
    const encrypted = "FGo0I6QjtSlH30bgBx0Sqw==";
    const decryptedData = await Decrypt(encrypted);
    expect(decryptedData).toBe(data);
  });
});
