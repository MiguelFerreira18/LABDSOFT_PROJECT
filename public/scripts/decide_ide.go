package main

import (
	"fmt"
	"os"
	"os/exec"
	"time"
)

func main() {
	fmt.Println("Opening IDE for your OS")
	isNextNeeded, err := test_for_ide_existence("android")
	if err != nil {
		fmt.Println("YOU DON't Have Android Studio: ", err)
	}
	if !isNextNeeded {
		_, err := test_for_ide_existence("ios")
		if err != nil {
			fmt.Println("YOU DON't Have XCode: ", err)
		}
	}

	time.Sleep(time.Second * 20)

}

func test_for_ide_existence(ide string) (bool, error) {
	cmd := exec.Command("npx", "cap", "open", ide)
	cmd.Stderr = os.Stderr
	cmd.Stdout = os.Stdout
	err := cmd.Start()
	if err != nil {
		return false, err
	}
	return true, nil
}
