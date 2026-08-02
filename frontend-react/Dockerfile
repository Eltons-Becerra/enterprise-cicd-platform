# ==========================================
# Etapa 1: construcción del frontend
# ==========================================
FROM node:22-alpine AS builder

WORKDIR /app

# Se copian primero los archivos de dependencias
COPY package.json package-lock.json ./

RUN npm ci

# Se copia el resto del código
COPY . .

# Genera la carpeta /app/dist
RUN npm run build


# ==========================================
# Etapa 2: servidor web
# ==========================================
FROM nginx:alpine

# Elimina la configuración predeterminada
RUN rm /etc/nginx/conf.d/default.conf

# Copia nuestra configuración
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

# Copia el resultado compilado de React
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]