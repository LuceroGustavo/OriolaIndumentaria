#!/usr/bin/env python3
"""
Script para descargar la página de detalle de producto de Lovely Denim
Referencia: https://www.lovelydenim.com.ar/camisa-larga-spotlight/p?skuId=1008283
"""

import requests
import os
from urllib.parse import urljoin, urlparse
from bs4 import BeautifulSoup
import time
import re

def create_directory_structure():
    """Crear la estructura de directorios para la referencia"""
    base_dir = "lovelydenim-product-detail-reference"
    subdirs = ["css", "js", "images", "fonts"]
    
    if not os.path.exists(base_dir):
        os.makedirs(base_dir)
        for subdir in subdirs:
            os.makedirs(os.path.join(base_dir, subdir))
        print(f"✅ Directorio creado: {base_dir}")
    else:
        print(f"📁 Directorio ya existe: {base_dir}")
    
    return base_dir

def download_file(url, filepath, session):
    """Descargar un archivo individual"""
    try:
        print(f"📥 Descargando: {url}")
        response = session.get(url, timeout=30)
        response.raise_for_status()
        
        with open(filepath, 'wb') as f:
            f.write(response.content)
        print(f"✅ Guardado: {filepath}")
        return True
    except Exception as e:
        print(f"❌ Error descargando {url}: {str(e)}")
        return False

def download_css_files(html_content, base_url, base_dir, session):
    """Descargar archivos CSS"""
    soup = BeautifulSoup(html_content, 'html.parser')
    css_links = soup.find_all('link', rel='stylesheet')
    
    print(f"\n🎨 Descargando {len(css_links)} archivos CSS...")
    
    for i, link in enumerate(css_links):
        href = link.get('href')
        if href:
            if href.startswith('http'):
                css_url = href
            else:
                css_url = urljoin(base_url, href)
            
            # Generar nombre de archivo único
            parsed_url = urlparse(css_url)
            filename = f"style_{i+1}.css"
            if '?' in parsed_url.path:
                filename = f"style_{i+1}_{parsed_url.path.split('/')[-1].split('?')[0]}.css"
            
            filepath = os.path.join(base_dir, "css", filename)
            download_file(css_url, filepath, session)
            time.sleep(0.5)  # Pausa entre descargas

def download_js_files(html_content, base_url, base_dir, session):
    """Descargar archivos JavaScript"""
    soup = BeautifulSoup(html_content, 'html.parser')
    js_scripts = soup.find_all('script', src=True)
    
    print(f"\n⚙️ Descargando {len(js_scripts)} archivos JavaScript...")
    
    for i, script in enumerate(js_scripts):
        src = script.get('src')
        if src:
            if src.startswith('http'):
                js_url = src
            else:
                js_url = urljoin(base_url, src)
            
            # Generar nombre de archivo único
            parsed_url = urlparse(js_url)
            filename = f"script_{i+1}.js"
            if '?' in parsed_url.path:
                filename = f"script_{i+1}_{parsed_url.path.split('/')[-1].split('?')[0]}.js"
            
            filepath = os.path.join(base_dir, "js", filename)
            download_file(js_url, filepath, session)
            time.sleep(0.5)

def download_images(html_content, base_url, base_dir, session):
    """Descargar imágenes"""
    soup = BeautifulSoup(html_content, 'html.parser')
    images = soup.find_all('img')
    
    print(f"\n🖼️ Descargando {len(images)} imágenes...")
    
    for i, img in enumerate(images):
        src = img.get('src')
        if src:
            if src.startswith('http'):
                img_url = src
            else:
                img_url = urljoin(base_url, src)
            
            # Generar nombre de archivo único
            parsed_url = urlparse(img_url)
            filename = f"image_{i+1}.jpg"
            if '.' in parsed_url.path:
                ext = parsed_url.path.split('.')[-1].split('?')[0]
                filename = f"image_{i+1}.{ext}"
            
            filepath = os.path.join(base_dir, "images", filename)
            download_file(img_url, filepath, session)
            time.sleep(0.3)

def clean_html_for_reference(html_content, base_url):
    """Limpiar el HTML para uso como referencia"""
    soup = BeautifulSoup(html_content, 'html.parser')
    
    # Remover scripts y estilos inline problemáticos
    for script in soup.find_all('script'):
        script.decompose()
    
    # Actualizar referencias a archivos locales
    for link in soup.find_all('link', rel='stylesheet'):
        href = link.get('href')
        if href and not href.startswith('http'):
            link['href'] = urljoin(base_url, href)
    
    for img in soup.find_all('img'):
        src = img.get('src')
        if src and not src.startswith('http'):
            img['src'] = urljoin(base_url, src)
    
    return str(soup)

def main():
    """Función principal"""
    print("🚀 Iniciando descarga de referencia de detalle de producto Lovely Denim")
    print("=" * 70)
    
    # URL del producto
    product_url = "https://www.lovelydenim.com.ar/camisa-larga-spotlight/p?skuId=1008283"
    
    # Crear estructura de directorios
    base_dir = create_directory_structure()
    
    # Configurar sesión con headers realistas
    session = requests.Session()
    session.headers.update({
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
        'Accept-Language': 'es-AR,es;q=0.9,en;q=0.8',
        'Accept-Encoding': 'gzip, deflate, br',
        'Connection': 'keep-alive',
        'Upgrade-Insecure-Requests': '1',
    })
    
    try:
        print(f"🌐 Descargando página principal: {product_url}")
        response = session.get(product_url, timeout=30)
        response.raise_for_status()
        
        html_content = response.text
        print(f"✅ Página descargada ({len(html_content)} caracteres)")
        
        # Guardar HTML original
        html_file = os.path.join(base_dir, "lovelydenim-product-detail.html")
        with open(html_file, 'w', encoding='utf-8') as f:
            f.write(html_content)
        print(f"💾 HTML guardado: {html_file}")
        
        # Crear HTML limpio para referencia
        clean_html = clean_html_for_reference(html_content, product_url)
        clean_html_file = os.path.join(base_dir, "product-detail-reference.html")
        with open(clean_html_file, 'w', encoding='utf-8') as f:
            f.write(clean_html)
        print(f"🧹 HTML limpio guardado: {clean_html_file}")
        
        # Descargar recursos
        download_css_files(html_content, product_url, base_dir, session)
        download_js_files(html_content, product_url, base_dir, session)
        download_images(html_content, product_url, base_dir, session)
        
        # Crear archivo de metadatos
        metadata = {
            "source_url": product_url,
            "download_date": time.strftime("%Y-%m-%d %H:%M:%S"),
            "description": "Referencia de página de detalle de producto Lovely Denim",
            "product_name": "Camisa Larga Spotlight",
            "files": {
                "html_original": "lovelydenim-product-detail.html",
                "html_clean": "product-detail-reference.html",
                "css_folder": "css/",
                "js_folder": "js/",
                "images_folder": "images/"
            }
        }
        
        import json
        metadata_file = os.path.join(base_dir, "metadata.json")
        with open(metadata_file, 'w', encoding='utf-8') as f:
            json.dump(metadata, f, indent=2, ensure_ascii=False)
        print(f"📋 Metadatos guardados: {metadata_file}")
        
        print("\n" + "=" * 70)
        print("🎉 ¡Descarga completada exitosamente!")
        print(f"📁 Directorio de referencia: {base_dir}")
        print("📄 Archivos principales:")
        print(f"   - {html_file}")
        print(f"   - {clean_html_file}")
        print(f"   - {metadata_file}")
        print("=" * 70)
        
    except Exception as e:
        print(f"❌ Error durante la descarga: {str(e)}")
        return False
    
    return True

if __name__ == "__main__":
    success = main()
    if success:
        print("\n✅ Script ejecutado exitosamente")
    else:
        print("\n❌ Script falló")
