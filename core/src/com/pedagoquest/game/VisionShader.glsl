#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform vec2 u_center;
uniform float u_radius;

void main()
{
    vec2 fragCoord = gl_FragCoord.xy / u_resolution;

    // Convert center and radius to screen coordinates
    vec2 center = u_center / u_resolution;
    float radius = u_radius / min(u_resolution.x, u_resolution.y);

    // Calculate the distance from the fragment to the center of the circle
    float distanceToCenter = distance(center, fragCoord);

    // Calculate alpha based on the distance to the center and the radius
    float alpha = smoothstep(radius, radius + 0.15, distanceToCenter);

    // Apply darkness effect based on alpha
    vec4 texColor = v_color * texture2D(u_texture, v_texCoords);
    gl_FragColor = mix(texColor, vec4(0.0, 0.0, 0.0, 0.0), alpha);
}