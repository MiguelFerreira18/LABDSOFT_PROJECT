package main

import (
	"bufio"
	"flag"
	"fmt"
	"os"
	"os/exec"
	"strings"
)

func main() {
	testFlag := flag.Bool("test", false, "enable test mode")

	flag.Parse()
	fmt.Println("DEFINING OS ENV")

	hasSetEnvs, err := setEnvs()
	if err != nil && !hasSetEnvs {
		fmt.Println("Environment varaibles were not set due to: ", err)
		return
	}

	hasRunMaven, err := packageJava(*testFlag)
	if err != nil && !hasRunMaven {
		fmt.Println("An Error occurred running the maven package: ", err)
		return
	}
	if !*testFlag {
		hasRunJavaAPI, err := runJavaAPI()
		if err != nil && !hasRunJavaAPI {
			fmt.Println("An Error occurred running the run command for the jar: ", err)
		}
	}
}

func runJavaAPI() (bool, error) {
	javaCmd := exec.Command("java", "-Dspring.profiles.active=dev", "-jar", "./target/SmartCityAPI.jar")
	javaCmd.Stdout = os.Stdout
	javaCmd.Stderr = os.Stderr
	javaCmd.Env = os.Environ()
	err := javaCmd.Run()
	if err != nil {
		return false, err
	}
	return true, nil
}

func packageJava(shouldTest bool) (bool, error) {
	var mvnCmd *exec.Cmd
	if shouldTest {
		mvnCmd = exec.Command("mvn", "package")
	} else {
		mvnCmd = exec.Command("mvn", "package", "-DskipTests")
	}
	mvnCmd.Stdout = os.Stdout
	mvnCmd.Stderr = os.Stderr
	err := mvnCmd.Run()
	if err != nil {
		return false, err
	}
	return true, nil
}

func setEnvs() (bool, error) {
	envMap, err := fetchEnvs()
	if err != nil {
		return false, err
	}
	for k, v := range envMap {
		if err := os.Setenv(k, v); err != nil {
			return false, nil
		}
	}
	return true, nil
}

func fetchEnvs() (map[string]string, error) {
	fileLines, err := loadEnvFile()
	if err != nil {
		return nil, err
	}
	envMap := make(map[string]string)

	for _, line := range fileLines {
		lineSplit := strings.Split(line, "=")
		envMap[lineSplit[0]] = lineSplit[1]
	}

	return envMap, nil
}

func loadEnvFile() ([]string, error) {
	file, err := os.Open("./.env")
	if err != nil {
		fmt.Println("Didn't find the .env file")
		return nil, err
	}
	var fileLines []string
	fileScanner := bufio.NewScanner(file)
	fileScanner.Split(bufio.ScanLines)

	for fileScanner.Scan() {
		fileLines = append(fileLines, fileScanner.Text())

	}
	defer file.Close()

	return fileLines, nil
}
