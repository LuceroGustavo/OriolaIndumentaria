#!/usr/bin/env python3
"""
Script para descargar completamente la página de Lovely Denim
Incluye HTML, CSS, JS, imágenes y otros recursos
"""

import requests
import os
import re
from urllib.parse import urljoin, urlparse
from bs4 import BeautifulSoup
import time
import json

def create_directory(path):
    """Crear directorio si no existe"""
    if not os.path.exists(path):
        os.makedirs(path)
        print(f"✅ Directorio creado: {path}")

def download_file(url, filepath):
    """Descargar un archivo individual"""
    try:
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        }
        
        response = requests.get(url, headers=headers, timeout=30)
        response.raise_for_status()
        
        with open(filepath, 'wb') as f:
            f.write(response.content)
        
        print(f"✅ Descargado: {os.path.basename(filepath)}")
        return True
        
    except Exception as e:
        print(f"❌ Error descargando {url}: {str(e)}")
        return False

def extract_resources(html_content, base_url):
    """Extraer todos los recursos (CSS, JS, imágenes) del HTML"""
    soup = BeautifulSoup(html_content, 'html.parser')
    resources = {
        'css': [],
        'js': [],
        'images': [],
        'fonts': []
    }
    
    # Extraer CSS
    for link in soup.find_all('link', rel='stylesheet'):
        href = link.get('href')
        if href:
            resources['css'].append(urljoin(base_url, href))
    
    # Extraer JavaScript
    for script in soup.find_all('script', src=True):
        src = script.get('src')
        if src:
            resources['js'].append(urljoin(base_url, src))
    
    # Extraer imágenes
    for img in soup.find_all('img', src=True):
        src = img.get('src')
        if src:
            resources['images'].append(urljoin(base_url, src))
    
    # Extraer fuentes
    for link in soup.find_all('link', rel=['preload', 'prefetch']):
        href = link.get('href')
        if href and any(ext in href.lower() for ext in ['.woff', '.woff2', '.ttf', '.otf']):
            resources['fonts'].append(urljoin(base_url, href))
    
    return resources

def download_lovelydenim():
    """Función principal para descargar Lovely Denim"""
    base_url = "https://www.lovelydenim.com.ar"
    target_url = f"{base_url}/menorca-stories"
    
    # Crear estructura de directorios
    base_dir = "lovelydenim-reference"
    create_directory(base_dir)
    create_directory(f"{base_dir}/css")
    create_directory(f"{base_dir}/js")
    create_directory(f"{base_dir}/images")
    create_directory(f"{base_dir}/fonts")
    
    print("🚀 Iniciando descarga de Lovely Denim...")
    print(f"📄 URL objetivo: {target_url}")
    
    # 1. Descargar HTML principal
    print("\n📄 Descargando HTML principal...")
    if not download_file(target_url, f"{base_dir}/index.html"):
        print("❌ Error descargando HTML principal")
        return
    
    # 2. Leer HTML y extraer recursos
    print("\n🔍 Analizando recursos...")
    with open(f"{base_dir}/index.html", 'r', encoding='utf-8') as f:
        html_content = f.read()
    
    resources = extract_resources(html_content, base_url)
    
    print(f"📊 Recursos encontrados:")
    print(f"   - CSS: {len(resources['css'])} archivos")
    print(f"   - JS: {len(resources['js'])} archivos")
    print(f"   - Imágenes: {len(resources['images'])} archivos")
    print(f"   - Fuentes: {len(resources['fonts'])} archivos")
    
    # 3. Descargar CSS
    print("\n🎨 Descargando archivos CSS...")
    for i, css_url in enumerate(resources['css'], 1):
        filename = f"style_{i}.css"
        filepath = f"{base_dir}/css/{filename}"
        download_file(css_url, filepath)
        time.sleep(0.5)  # Pausa para no sobrecargar el servidor
    
    # 4. Descargar JavaScript
    print("\n⚡ Descargando archivos JavaScript...")
    for i, js_url in enumerate(resources['js'], 1):
        filename = f"script_{i}.js"
        filepath = f"{base_dir}/js/{filename}"
        download_file(js_url, filepath)
        time.sleep(0.5)
    
    # 5. Descargar imágenes (solo las primeras 20 para no sobrecargar)
    print("\n🖼️ Descargando imágenes...")
    for i, img_url in enumerate(resources['images'][:20], 1):
        filename = f"image_{i}.{img_url.split('.')[-1].split('?')[0]}"
        filepath = f"{base_dir}/images/{filename}"
        download_file(img_url, filepath)
        time.sleep(0.5)
    
    # 6. Descargar fuentes
    print("\n🔤 Descargando fuentes...")
    for i, font_url in enumerate(resources['fonts'], 1):
        filename = f"font_{i}.{font_url.split('.')[-1].split('?')[0]}"
        filepath = f"{base_dir}/fonts/{filename}"
        download_file(font_url, filepath)
        time.sleep(0.5)
    
    # 7. Guardar metadatos
    metadata = {
        'url': target_url,
        'download_date': time.strftime('%Y-%m-%d %H:%M:%S'),
        'resources': {
            'css_count': len(resources['css']),
            'js_count': len(resources['js']),
            'images_count': len(resources['images']),
            'fonts_count': len(resources['fonts'])
        }
    }
    
    with open(f"{base_dir}/metadata.json", 'w', encoding='utf-8') as f:
        json.dump(metadata, f, indent=2, ensure_ascii=False)
    
    print(f"\n🎉 ¡Descarga completada!")
    print(f"📁 Archivos guardados en: {base_dir}/")
    print(f"📄 HTML principal: {base_dir}/index.html")
    print(f"📊 Metadatos: {base_dir}/metadata.json")

if __name__ == "__main__":
    try:
        download_lovelydenim()
    except KeyboardInterrupt:
        print("\n⏹️ Descarga cancelada por el usuario")
    except Exception as e:
        print(f"\n❌ Error inesperado: {str(e)}")
