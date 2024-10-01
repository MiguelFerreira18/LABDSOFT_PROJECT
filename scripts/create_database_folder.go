package main

import (
	"fmt"
	"log"
	"os"
	"os/exec"
	"path/filepath"
)

func main() {
	databaseFolder := "../TempDatabase"
	envSource := "./template.env"
	envDestination := filepath.Join(databaseFolder, ".env")
	composeSource := "./database-docker-compose.yml"
	composeDest := filepath.Join(databaseFolder, "docker-compose.yml")

	fmt.Println("Creating temporary database directory")
	if err := os.MkdirAll(databaseFolder, os.ModePerm); err != nil {
		log.Fatalf("Failed to create directory: %v", err)
	}
	fmt.Println("Copying .env file")
	if err := copyFile(envSource, envDestination); err != nil {
		log.Fatalf("Failed to copy .env file: %v", err)
	}
	fmt.Println("copying docker-compose.yml file")
	if err := copyFile(composeSource, composeDest); err != nil {
		log.Fatalf("Failed to copy docker-compose.yml: %v", err)
	}
	fmt.Println("Running docker-compose up")
	if err := runDockerCompose(databaseFolder); err != nil {
		log.Fatalf("Failed to run docker-compose: ", err)
	}
}

func copyFile(source, dest string) error {
	sourceFile, err := os.Open(source)
	if err != nil {
		return err
	}
	defer sourceFile.Close()

	destFile, err := os.Create(dest)
	if err != nil {
		return err
	}
	defer destFile.Close()

	_, err = destFile.ReadFrom(sourceFile)
	if err != nil {
		return err
	}

	return nil
}

// Helper function to run docker-compose command
func runDockerCompose(dir string) error {
	cmd := exec.Command("docker-compose", "--env-file", ".env", "up")
	cmd.Dir = dir
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	return cmd.Run()
}
