#!/bin/bash
# Runtime Testing Script for Clish
# Usage: ./test.sh [unit|integration|client|all]

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo_step() {
    echo -e "${GREEN}==>${NC} $1"
}

echo_warn() {
    echo -e "${YELLOW}==> WARNING:${NC} $1"
}

# Ensure scripts directory exists
SCRIPTS_DIR="$PROJECT_DIR/config/clish/scripts"
mkdir -p "$SCRIPTS_DIR"

# Copy test scripts to runtime scripts directory
copy_test_scripts() {
    echo_step "Copying test scripts to runtime directory..."
    if [ -d "$PROJECT_DIR/src/test/resources/test-scripts" ]; then
        cp -v "$PROJECT_DIR/src/test/resources/test-scripts/"*.clish "$SCRIPTS_DIR/"
    else
        echo_warn "No test scripts found in src/test/resources/test-scripts/"
    fi
}

# Run unit tests
run_unit_tests() {
    echo_step "Running unit tests..."
    ./gradlew test --info 2>&1 | tail -20
}

# Run integration test in development client
run_integration_client() {
    echo_step "Starting development client for integration testing..."
    echo -e "${YELLOW}Note:${NC} In-game, run: /clish run integration-test"
    echo -e "${YELLOW}Note:${NC} Check logs at: run/logs/latest.log"
    ./gradlew runClient
}

# Main selection
case "${1:-all}" in
    unit)
        run_unit_tests
        ;;
    integration)
        copy_test_scripts
        run_integration_client
        ;;
    client)
        run_integration_client
        ;;
    all|"")
        echo_step "Running all tests..."
        echo ""
        run_unit_tests
        echo ""
        copy_test_scripts
        echo ""
        echo_step "Unit tests passed. Starting client for integration testing..."
        echo -e "${YELLOW}In-game:${NC}"
        echo -e "  1. Run: ${GREEN}/clish run integration-test${NC}"
        echo -e "  2. Check output in chat"
        echo -e "  3. View logs at: ${YELLOW}run/logs/latest.log${NC}"
        echo ""
        run_integration_client
        ;;
    *)
        echo "Usage: $0 [unit|integration|client|all]"
        echo ""
        echo "  unit        - Run unit tests only"
        echo "  integration - Copy test scripts and start client"
        echo "  client      - Start development client"
        echo "  all         - Run all tests (default)"
        exit 1
        ;;
esac
