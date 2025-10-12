#!/bin/bash
# menu-deploy.sh - Menú interactivo para gestión de OriolaIndumentaria
# Servidor: LightNode - 149.104.92.116

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Función para imprimir con colores
print_header() {
    echo -e "${CYAN}=========================================="
    echo -e "   ORIOLA INDUMENTARIA - MENÚ DE GESTIÓN"
    echo -e "==========================================${NC}"
}

print_option() {
    echo -e "${BLUE}$1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Función para verificar si la aplicación está corriendo
check_app_status() {
    if pgrep -f "oriola-denim-0.0.1-SNAPSHOT.jar" > /dev/null; then
        PID=$(pgrep -f "oriola-denim-0.0.1-SNAPSHOT.jar")
        echo -e "${GREEN}🟢 Aplicación corriendo (PID: $PID)${NC}"
        return 0
    else
        echo -e "${RED}🔴 Aplicación no está corriendo${NC}"
        return 1
    fi
}

# Función para parar la aplicación
stop_app() {
    print_option "1️⃣ Parando aplicación..."
    if pgrep -f "oriola-denim-0.0.1-SNAPSHOT.jar" > /dev/null; then
        pkill -f "oriola-denim-0.0.1-SNAPSHOT.jar"
        sleep 3
        
        if pgrep -f "oriola-denim-0.0.1-SNAPSHOT.jar" > /dev/null; then
            print_warning "Forzando cierre..."
            pkill -9 -f "oriola-denim-0.0.1-SNAPSHOT.jar"
        fi
        print_success "Aplicación detenida"
    else
        print_warning "No hay aplicación corriendo"
    fi
}

# Función para actualizar código
update_code() {
    print_option "2️⃣ Actualizando código desde GitHub..."
    git fetch origin
    if git pull origin master; then
        print_success "Código actualizado"
    else
        print_error "Error al actualizar código"
    fi
}

# Función para compilar
compile_app() {
    print_option "3️⃣ Compilando aplicación..."
    print_warning "Esto puede tomar varios minutos..."
    if mvn clean package -DskipTests; then
        print_success "Compilación exitosa"
    else
        print_error "Error en la compilación"
    fi
}

# Función para iniciar aplicación
start_app() {
    print_option "4️⃣ Iniciando aplicación..."
    if [ ! -f "target/oriola-denim-0.0.1-SNAPSHOT.jar" ]; then
        print_error "No se encontró el JAR. Compila primero (opción 3)"
        return 1
    fi
    
    nohup java -jar target/oriola-denim-0.0.1-SNAPSHOT.jar --spring.profiles.active=lightnode > /dev/null 2>&1 &
    sleep 5
    
    if check_app_status; then
        print_success "Aplicación iniciada correctamente"
        echo -e "${CYAN}🌐 Disponible en: http://149.104.92.116:8080${NC}"
    else
        print_error "Error al iniciar la aplicación"
    fi
}

# Función para despliegue completo
full_deploy() {
    print_option "5️⃣ Despliegue completo iniciado..."
    stop_app
    update_code
    compile_app
    start_app
    echo ""
    print_success "🎉 Despliegue completo finalizado"
}

# Función para ver logs
view_logs() {
    print_option "7️⃣ Mostrando logs de la aplicación..."
    if [ -f "nohup.out" ]; then
        echo -e "${YELLOW}Últimas 50 líneas de logs:${NC}"
        tail -50 nohup.out
        echo ""
        echo -e "${BLUE}Para ver logs en tiempo real: tail -f nohup.out${NC}"
    else
        print_warning "No se encontró archivo de logs"
    fi
}

# Función para ver estado del sistema
system_status() {
    print_option "6️⃣ Estado del sistema:"
    echo ""
    echo -e "${CYAN}=== APLICACIÓN ===${NC}"
    check_app_status
    echo ""
    echo -e "${CYAN}=== RECURSOS DEL SERVIDOR ===${NC}"
    echo "Uso de CPU y memoria:"
    top -bn1 | head -5
    echo ""
    echo -e "${CYAN}=== ESPACIO EN DISCO ===${NC}"
    df -h | grep -E "(Filesystem|/dev/)"
    echo ""
    echo -e "${CYAN}=== MEMORIA ===${NC}"
    free -h
}

# Función para reiniciar aplicación
restart_app() {
    print_option "8️⃣ Reiniciando aplicación..."
    stop_app
    sleep 2
    start_app
}

# Función para ver información del proyecto
project_info() {
    print_option "9️⃣ Información del proyecto:"
    echo ""
    echo -e "${CYAN}=== INFORMACIÓN GIT ===${NC}"
    echo "Rama actual: $(git branch --show-current)"
    echo "Último commit: $(git log -1 --oneline)"
    echo "Estado: $(git status --porcelain | wc -l) archivos modificados"
    echo ""
    echo -e "${CYAN}=== INFORMACIÓN DEL PROYECTO ===${NC}"
    echo "Directorio: $(pwd)"
    echo "JAR disponible: $([ -f "target/oriola-denim-0.0.1-SNAPSHOT.jar" ] && echo "Sí" || echo "No")"
    echo "Tamaño del JAR: $([ -f "target/oriola-denim-0.0.1-SNAPSHOT.jar" ] && du -h target/oriola-denim-0.0.1-SNAPSHOT.jar | cut -f1 || echo "N/A")"
}

# Menú principal
while true; do
    clear
    print_header
    echo ""
    check_app_status
    echo ""
    print_option "Selecciona una opción:"
    echo ""
    echo "1.  Parar aplicación"
    echo "2.  Actualizar código (git pull)"
    echo "3.  Compilar aplicación"
    echo "4.  Iniciar aplicación"
    echo "5.  Despliegue completo (1+2+3+4)"
    echo "6.  Ver estado del sistema"
    echo "7.  Ver logs de la aplicación"
    echo "8.  Reiniciar aplicación"
    echo "9.  Información del proyecto"
    echo "10. Salir"
    echo ""
    read -p "Ingresa tu opción (1-10): " opcion
    echo ""
    
    case $opcion in
        1) stop_app ;;
        2) update_code ;;
        3) compile_app ;;
        4) start_app ;;
        5) full_deploy ;;
        6) system_status ;;
        7) view_logs ;;
        8) restart_app ;;
        9) project_info ;;
        10) 
            print_success "Saliendo del menú..."
            exit 0 
            ;;
        *) 
            print_error "Opción inválida. Presiona Enter para continuar..."
            read
            ;;
    esac
    
    echo ""
    read -p "Presiona Enter para continuar..."
done
