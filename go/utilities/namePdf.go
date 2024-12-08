package utilities

import (
	"fmt"
	"time"
)

func NameFileTime(baseName string) string {
	timestamp := time.Now().Format("20060102_150405")
	return fmt.Sprintf("%s_%s", baseName, timestamp)
}
